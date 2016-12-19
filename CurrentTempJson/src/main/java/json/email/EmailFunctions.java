package json.email;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import json.pdf.Pdf;

public class EmailFunctions {

	private static Logger logger = Logger.getLogger(EmailFunctions.class);
	private static Scanner scanner = new Scanner(System.in);

	public static ArrayList<String> toAddress() {

		logger.info("Send to(for multiple entry separate with space):");
		ArrayList<String> address = new ArrayList<String>();

		String[] a = scanner.nextLine().split(" ");
		for (int i = 0; i < a.length; i++) {
			address.add(a[i]);
			logger.info(i + 1 + ". " + a[i] + "\n");
		}

		return address;
	}

	public static String subject() {

		logger.info("Please enter a subject:");
		String sub = scanner.nextLine();
		logger.info(sub + "\n");
		return sub;
	}

	public static String message() {

		logger.info("Please enter the message:");
		String msg = scanner.nextLine();
		logger.info(msg + "\n");
		return msg;
	}

	public static ArrayList<String> attachFiles() {
	
		logger.info("Enter generated files..(Press 0 to send e-mail):");
		ArrayList<String> att = new ArrayList<String>();
		boolean check = true;
		int count = 1;
		String filePath = "src/main/resources/pdf/";
		String ext = ".pdf";
		String fileName = null;
		while (check) {
			logger.info(count + ". " + (fileName = scanner.nextLine()));
			if (fileName.matches("0")) {
				logger.info("Sending e-mail!!");
				break;
			}
			att.add(filePath + fileName + ext);
			count++;
		}
		return att;
	}

	public static void sendEmail(ArrayList<String> toAddress, String subject, String message,
			ArrayList<String> attachFiles) throws Exception {

		Properties properties = new Properties();

		File file = new File("src/main/resources/properties/email.properties");
		Pdf.inputStream(properties, file);

		final String host = properties.getProperty("host");
		final String port = properties.getProperty("port");
		final String userName = properties.getProperty("userName");
		final String password = properties.getProperty("password");

		// SMTP server
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", port);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.user", userName);
		properties.put("mail.password", password);

		// creates a new session with an authenticator
		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, password);
			}
		};
		Session session = Session.getInstance(properties, auth);

		// creates a new e-mail message
		Message msg = new MimeMessage(session);

		msg.setFrom(new InternetAddress(userName));
		InternetAddress[] toAddresses = new InternetAddress[toAddress.size()];
		for (int i = 0; i < toAddress.size(); i++) {
			toAddresses[i] = new InternetAddress(toAddress.get(i));
		}
		msg.setRecipients(Message.RecipientType.TO, toAddresses);
		msg.setSubject(subject);
		msg.setSentDate(new Date());

		// creates message part
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(message, "text/html");

		// creates multi-part
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);

		// adds attachments
		if (attachFiles != null) {
			for (String filePath : attachFiles) {
				MimeBodyPart attachPart = new MimeBodyPart();

				attachPart.attachFile(filePath);

				multipart.addBodyPart(attachPart);
			}
		}

		// sets the multi-part as e-mail's content
		msg.setContent(multipart);

		// sends the e-mail
		Transport.send(msg);

	}

}
