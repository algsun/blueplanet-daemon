package com.microwise.msp.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

/**
 * @author bastengao
 * @date 14-4-29 上午9:30
 */
@Component
public class EmailSender {

    public static final ConfigFactory.Configs config = ConfigFactory.getInstance().getConfig("email-config.properties");

    @Qualifier("commonMailSender")
    @Autowired
    private org.springframework.mail.MailSender mailSender;

    public void send(String recipient, String title, String content) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = ((JavaMailSender) mailSender).createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        String smtpFrom = config.get("smtp.from");
        String smtpAuthor = config.get("smtp.author");
        helper.setFrom(smtpFrom, smtpAuthor);
        helper.setTo(recipient);
        helper.setSubject(title);
        helper.setText(content, true);
        ((JavaMailSender) mailSender).send(mimeMessage);
    }
}
