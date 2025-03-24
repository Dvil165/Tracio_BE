package com.dvil.tracio.service;

import com.dvil.tracio.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    @Autowired
    private JavaMailSender EmailSender;

    public void sendVerifyCode(User user, String token, String title, String subject) {
        try {
            SimpleMailMessage email = new SimpleMailMessage();
            String recipientAddress = user.getEmail();
            email.setTo(recipientAddress);
            email.setText(title + "\n" + subject + "\n" + token);
            email.setSubject(subject);
            EmailSender.send(email);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void sendEmail(String to, String subject, String content) {
        try {
            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(to);
            email.setSubject(subject);
            email.setText(content);
            EmailSender.send(email);
        } catch (Exception e) {
            logger.error("Error while sending email: ", e);
        }
    }

}
