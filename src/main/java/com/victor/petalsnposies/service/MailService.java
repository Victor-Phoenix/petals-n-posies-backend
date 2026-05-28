package com.victor.petalsnposies.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.victor.petalsnposies.controller.FlowerController;
import com.victor.petalsnposies.dto.EmailRequestDTO;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {

	@Autowired
	private JavaMailSender mailSender;
//
//    MailService(JavaMailSender mailSender) {
//        this.mailSender = mailSender;
//    }

	public void sendPlainText(EmailRequestDTO emailRequest) {
//		SimpleMailMessage message = new SimpleMailMessage();
//		message.setTo(emailRequest.getTo());
//		message.setSubject(emailRequest.getSubject());
//		message.setText(emailRequest.getBody());
//		mailSender.send(message);
		
	}
	
	public void sendHtml(String to, String subject, String htmlBody) throws MessagingException{
//		MimeMessage message = mailSender.createMimeMessage();
//		MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
//		helper.setTo(to);
//		helper.setSubject(subject);
//		helper.setText(htmlBody,true);
//		mailSender.send(message);
		
	}
}
