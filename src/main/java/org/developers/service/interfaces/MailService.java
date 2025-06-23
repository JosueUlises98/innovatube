package org.developers.service.interfaces;

import java.util.Map;

public interface MailService {

    void sendHtmlEmail(String to, String subject, String templateName,
                       Map<String, Object> parameters);

    void sendSimpleHtmlEmail(String to, String subject, String htmlBody);

    void sendEmailWithAttachment(String to, String subject, String htmlBody,
                                 byte[] attachmentData, String attachmentName);

    void sendEmailWithCCAndBCC(String to, String cc, String bcc,
                               String subject, String htmlBody);

}
