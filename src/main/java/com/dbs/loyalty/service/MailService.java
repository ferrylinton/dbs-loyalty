package com.dbs.loyalty.service;

import java.nio.charset.StandardCharsets;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.config.ApplicationProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for sending emails.
 * <p>
 * We use the @Async annotation to send emails asynchronously.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MailService {
	
	private static final String TOKEN_FORMAT = "Verification Token : %d";
	
	private static final String SUBJECT_TOKEN = "DBS";

    private final ApplicationProperties applicationProperties;

    private final JavaMailSender javaMailSender;

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(applicationProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
        	log.warn("Email could not be sent to user '{}': {}", to, e.getMessage());
        }
    }

    @Async
    public void sendToken(String to, Integer token) {
        sendEmail(to, SUBJECT_TOKEN, String.format(TOKEN_FORMAT, token), false, false);
    }

    
}

