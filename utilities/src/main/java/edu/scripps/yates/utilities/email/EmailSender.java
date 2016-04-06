package edu.scripps.yates.utilities.email;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

public class EmailSender {
	private static final Logger log = Logger.getLogger(EmailSender.class);

	/**
	 * Send an email specifying the to and the from of the sender and receiver
	 * respectivelly, by using the localhost and mail.smtp.host as host
	 * 
	 * @param subject
	 * @param body
	 * @param toemailAddress
	 * @param fromEmailAddress
	 * @return
	 */
	public static String sendEmail(String subject, String body,
			String toemailAddress, String fromEmailAddress) {
		String errorMessage = null;
		// Recipient's email ID needs to be mentioned.
		String to = toemailAddress;

		// Sender's email ID needs to be mentioned
		String from = fromEmailAddress;

		// Assuming you are sending email from localhost
		String host = "localhost";

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.setProperty("mail.smtp.host", host);

		log.info("Trying to send an email from " + fromEmailAddress + " to "
				+ toemailAddress + " with subject: " + subject + " and body: "
				+ body);
		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));

			// Set Subject: header field
			message.setSubject(subject);

			// Now set the actual message
			message.setText(body);

			// Send message
			Transport.send(message);
			log.info("Email sent message successfully");
		} catch (AddressException e) {
			errorMessage = "AddressException: " + e.getMessage();
		} catch (javax.mail.MessagingException e) {
			errorMessage = "MessagingException: " + e.getMessage();
		}
		if (errorMessage != null)
			log.error("Email was not sent due to error: " + errorMessage);
		return errorMessage;
	}
}
