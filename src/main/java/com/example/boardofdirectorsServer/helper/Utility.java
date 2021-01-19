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
	public String approveRequestUrl = "http://ifrsclient.herokuapp.com/RequeestApproved?id=";
	public String approveRequestUrlCompany = "http://ifrsclient.herokuapp.com/requestApprovedCompany?id=";
	public String resetPassword = "http://ifrsclient.herokuapp.com/resetPassword?id=";
	public String resetPasswordCompany = "http://ifrsclient.herokuapp.com/resetPasswordCompany?id=";

	public String sendEmail(String body, String sendTo, String cc, String subject) {
		String mailResponce;
		try {

			JavaMailSenderImpl sender = new JavaMailSenderImpl();
			sender.setHost("mail.host.com");
			MimeMessage msg = sender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(msg, true);

			helper.setTo(sendTo);
			if (!cc.isEmpty()) {
				helper.setCc(cc);
			}
			helper.setSubject(subject);
			helper.setText(body);

			// msg.setContent(body, "text/html");
			msg.setContent(body, "text/html; charset=utf-8");
			javaMailSender.send(msg);

			System.out.println("email sent to " + sendTo);
			mailResponce = "Success: email sent successfully.You will shortly receive responce";
		}

		catch (Exception e) {
			mailResponce = "Failure: sent successfully";
			throw new RuntimeException(e);
		}
		return mailResponce;

	}

}
