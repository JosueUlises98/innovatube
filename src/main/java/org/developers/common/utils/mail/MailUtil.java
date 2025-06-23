package org.developers.common.utils.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.developers.common.config.MailConfig;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class MailUtil {

    private MailUtil() {}

    private static final JavaMailSender mailSender = MailConfig.getMailSender();
    private static final TemplateEngine templateEngine = MailConfig.getTemplateEngine();
    private static final String EMAIL_ENCODING = StandardCharsets.UTF_8.name();

    public static void sendHtmlEmail(String to, String subject, String templateName,
                                     Map<String, Object> parameters) throws MessagingException {
        Context context = new Context();
        parameters.forEach(context::setVariable);

        String htmlContent = templateEngine.process(templateName, context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, EMAIL_ENCODING);

        helper.setFrom(MailConfig.getSenderEmail());
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    public static void sendSimpleHtmlEmail(String to, String subject, String htmlBody)
            throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, EMAIL_ENCODING);

        helper.setFrom(MailConfig.getSenderEmail());
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        mailSender.send(message);
    }

    public static void sendEmailWithAttachment(String to, String subject, String htmlBody,
                                               byte[] attachmentData, String attachmentName)
            throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, EMAIL_ENCODING);

        helper.setFrom(MailConfig.getSenderEmail());
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        helper.addAttachment(attachmentName, new ByteArrayResource(attachmentData));

        mailSender.send(message);
    }


    public static void sendEmailWithCCAndBCC(String to, String cc, String bcc,
                                             String subject, String htmlBody)
            throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, EMAIL_ENCODING);

        helper.setFrom(MailConfig.getSenderEmail());
        helper.setTo(to);
        if (cc != null && !cc.isEmpty()) {
            helper.setCc(cc);
        }
        if (bcc != null && !bcc.isEmpty()) {
            helper.setBcc(bcc);
        }
        helper.setSubject(subject);
        helper.setText(htmlBody, true);

        mailSender.send(message);
    }
}
