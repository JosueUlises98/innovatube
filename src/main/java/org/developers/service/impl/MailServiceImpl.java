package org.developers.service.impl;

import jakarta.mail.MessagingException;
import org.developers.common.utils.mail.MailUtil;
import org.developers.service.interfaces.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class MailServiceImpl implements MailService {

    private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    @Override
    public void sendHtmlEmail(String to, String subject, String templateName, Map<String, Object> parameters) {
        try {
            logger.info("Iniciando envío de correo HTML usando plantilla. Destinatario: {}, Asunto: {}, Plantilla: {}",
                    to, subject, templateName);

            MailUtil.sendHtmlEmail(to, subject, templateName, parameters);

            logger.info("Correo HTML enviado exitosamente a: {}", to);
        } catch (MessagingException e) {
            logger.error("Error al enviar correo HTML. Destinatario: {}, Plantilla: {}", to, templateName, e);
            throw new RuntimeException("Error al enviar correo HTML", e);
        }
    }

    @Override
    public void sendSimpleHtmlEmail(String to, String subject, String htmlBody) {
        try {
            logger.info("Iniciando envío de correo HTML simple. Destinatario: {}, Asunto: {}", to, subject);

            MailUtil.sendSimpleHtmlEmail(to, subject, htmlBody);

            logger.info("Correo HTML simple enviado exitosamente a: {}", to);
        } catch (MessagingException e) {
            logger.error("Error al enviar correo HTML simple. Destinatario: {}", to, e);
            throw new RuntimeException("Error al enviar correo HTML simple", e);
        }
    }

    @Override
    public void sendEmailWithAttachment(String to, String subject, String htmlBody,
                                        byte[] attachmentData, String attachmentName) {
        try {
            logger.info("Iniciando envío de correo con adjunto. Destinatario: {}, Asunto: {}, Nombre del adjunto: {}",
                    to, subject, attachmentName);

            MailUtil.sendEmailWithAttachment(to, subject, htmlBody, attachmentData, attachmentName);

            logger.info("Correo con adjunto enviado exitosamente a: {}", to);
        } catch (MessagingException e) {
            logger.error("Error al enviar correo con adjunto. Destinatario: {}, Nombre del adjunto: {}",
                    to, attachmentName, e);
            throw new RuntimeException("Error al enviar correo con adjunto", e);
        }
    }

    @Override
    public void sendEmailWithCCAndBCC(String to, String cc, String bcc, String subject, String htmlBody) {
        try {
            logger.info("Iniciando envío de correo con CC y BCC. Destinatario: {}, CC: {}, Asunto: {}",
                    to, cc, subject);

            MailUtil.sendEmailWithCCAndBCC(to, cc, bcc, subject, htmlBody);

            logger.info("Correo con CC y BCC enviado exitosamente a: {}", to);
        } catch (MessagingException e) {
            logger.error("Error al enviar correo con CC y BCC. Destinatario: {}, CC: {}", to, cc, e);
            throw new RuntimeException("Error al enviar correo con CC y BCC", e);
        }
    }
}
