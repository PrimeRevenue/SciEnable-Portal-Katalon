package smoke

import java.util.regex.Matcher
import java.util.regex.Pattern

import javax.mail.Address
import javax.mail.Folder
import javax.mail.Message
import javax.mail.Multipart
import javax.mail.Part
import javax.mail.Session
import javax.mail.Store
import javax.mail.search.ComparisonTerm
import javax.mail.search.ReceivedDateTerm

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.util.KeywordUtil

/**
 * Reads emails from a Gmail account over IMAP (Katalon's recommended approach for email/OTP,
 * mid-pilot review 10/8/2026). Needs a Google app password for the account, not its normal
 * password — Gmail rejects normal passwords over IMAP.
 */
class GmailReader {

	private static final List<String> FOLDERS = ['INBOX', '[Gmail]/Spam']

	/**
	 * Waits for an email sent to `recipient` whose subject contains `subjectContains`, received at or
	 * after `since`, checking the inbox and Spam every 10 seconds until `timeoutSeconds`. Returns
	 * capture group 1 of `regex` from the newest matching email's text; fails the test on timeout.
	 */
	@Keyword
	static String waitForValue(String gmailUser, String appPassword, String recipient, String subjectContains,
			String regex, Date since, int timeoutSeconds = 300) {
		Pattern pattern = Pattern.compile(regex)
		long deadline = System.currentTimeMillis() + timeoutSeconds * 1000L
		while (true) {
			String value = findValue(gmailUser, appPassword, recipient, subjectContains, pattern, since)
			if (value != null) {
				KeywordUtil.logInfo("Found email for ${recipient} ('${subjectContains}')")
				return value
			}
			if (System.currentTimeMillis() > deadline) {
				KeywordUtil.markFailedAndStop("No email for ${recipient} with subject containing '${subjectContains}' arrived within ${timeoutSeconds}s (checked inbox and Spam)")
			}
			Thread.sleep(10000)
		}
	}

	private static String findValue(String gmailUser, String appPassword, String recipient, String subjectContains,
			Pattern pattern, Date since) {
		Properties props = new Properties()
		props.put('mail.store.protocol', 'imaps')
		Store store = Session.getInstance(props).getStore('imaps')
		store.connect('imap.gmail.com', gmailUser, appPassword)
		try {
			// IMAP date search is day-granular, so narrow by day on the server and by time here.
			Date dayStart = new Date(since.time - 24L * 60 * 60 * 1000)
			Date newestMatch = null
			String newestValue = null
			for (String folderName : FOLDERS) {
				Folder folder = store.getFolder(folderName)
				if (!folder.exists()) {
					continue
				}
				folder.open(Folder.READ_ONLY)
				try {
					for (Message m : folder.search(new ReceivedDateTerm(ComparisonTerm.GE, dayStart))) {
						Date received = m.receivedDate
						if (received == null || received.before(new Date(since.time - 60000))) {
							continue
						}
						if (!(m.subject ?: '').toLowerCase().contains(subjectContains.toLowerCase())) {
							continue
						}
						if (!sentTo(m, recipient)) {
							continue
						}
						Matcher matcher = pattern.matcher(textOf(m))
						if (matcher.find() && (newestMatch == null || received.after(newestMatch))) {
							newestMatch = received
							newestValue = matcher.group(1)
						}
					}
				} finally {
					folder.close(false)
				}
			}
			return newestValue
		} finally {
			store.close()
		}
	}

	private static boolean sentTo(Message m, String recipient) {
		Address[] to = m.getRecipients(Message.RecipientType.TO) ?: new Address[0]
		return to.any { it.toString().toLowerCase().contains(recipient.toLowerCase()) }
	}

	private static String textOf(Part p) {
		if (p.isMimeType('text/plain')) {
			return p.content as String
		}
		if (p.isMimeType('text/html')) {
			// Keep href targets: links like Password Set Up live in <a href>, not the visible text.
			return (p.content as String).replaceAll(/(?i)<a[^>]+href="([^"]+)"[^>]*>/, ' $1 ').replaceAll(/<[^>]+>/, ' ')
					.replace('&amp;', '&')
		}
		if (p.isMimeType('multipart/*')) {
			Multipart mp = p.content as Multipart
			StringBuilder sb = new StringBuilder()
			for (int i = 0; i < mp.count; i++) {
				sb.append(textOf(mp.getBodyPart(i))).append('\n')
			}
			return sb.toString()
		}
		return ''
	}
}
