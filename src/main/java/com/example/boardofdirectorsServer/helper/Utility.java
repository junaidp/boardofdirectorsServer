package com.example.boardofdirectorsServer.helper;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class Utility {

	@Autowired
	private JavaMailSender javaMailSender;
	public String mainUrl = "http://3f67e325e8e6.ngrok.io";

	public boolean sendEmail(String body, String sendTo, String cc, String subject) {

		try {
			JavaMailSenderImpl sender = new JavaMailSenderImpl();
			sender.setHost("mail.host.com");
			MimeMessage msg = sender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(msg, true);

			helper.setTo(sendTo);

			helper.setSubject(subject);
			helper.setText(body);

			// msg.setContent(body, "text/html");
			msg.setContent(body, "text/html; charset=utf-8");
			javaMailSender.send(msg);

			System.out.println("email sent to " + sendTo);
			return true;
		}

		catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

}
