package com.dbs.loyalty.service;

import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.config.ApplicationProperties;

import lombok.RequiredArgsConstructor;

/**
 * Service for sending emails.
 * <p>
 * We use the @Async annotation to send emails asynchronously.
 */
@RequiredArgsConstructor
@Service
public class MailService {
	
	private static final String TOKEN_FORMAT = "Token : %d";
	
	private static final String SUBJECT_TOKEN = "DBS - Token";

    private final ApplicationProperties applicationProperties;

    private final JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
        message.setTo(to);
        message.setFrom(applicationProperties.getMail().getFrom());
        message.setSubject(subject);
        message.setText(content, isHtml);
        javaMailSender.send(mimeMessage);
    }

    public void sendToken(String to, Integer token) throws MessagingException {
        sendEmail(to, SUBJECT_TOKEN, String.format(TOKEN_FORMAT, token), false, false);
    }

    
}

