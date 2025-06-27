package unit.Mail;

import jakarta.mail.MessagingException;
import org.developers.common.utils.mail.MailUtil;
import org.developers.service.impl.MailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MailServiceTest {

    @InjectMocks
    private MailServiceImpl mailService;

    @Mock
    private Logger loggerMock;

    private static final String TO_EMAIL = "destinatario@ejemplo.com";
    private static final String SUBJECT = "Asunto de prueba";
    private static final String TEMPLATE_NAME = "plantilla-prueba.html";
    private static final String HTML_BODY = "<h1>Contenido HTML de prueba</h1>";
    private static final String CC_EMAIL = "cc@ejemplo.com";
    private static final String BCC_EMAIL = "bcc@ejemplo.com";
    private static final byte[] ATTACHMENT_DATA = "datos de prueba".getBytes();
    private static final String ATTACHMENT_NAME = "archivo.pdf";

    @BeforeEach
    void setUp() {
        try (MockedStatic<LoggerFactory> mockedStatic = mockStatic(LoggerFactory.class)) {
            mockedStatic.when(() -> LoggerFactory.getLogger(MailServiceImpl.class))
                    .thenReturn(loggerMock);
        }
    }

    @Test
    void sendHtmlEmail_DeberiaEnviarCorreoExitosamente() {
        // Arrange
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("parametro1", "valor1");

        try (MockedStatic<MailUtil> mailUtilMock = mockStatic(MailUtil.class)) {
            // Act
            mailService.sendHtmlEmail(TO_EMAIL, SUBJECT, TEMPLATE_NAME, parameters);

            // Assert
            mailUtilMock.verify(() ->
                    MailUtil.sendHtmlEmail(TO_EMAIL, SUBJECT, TEMPLATE_NAME, parameters));

            verify(loggerMock).info(
                    eq("Iniciando envío de correo HTML usando plantilla. Destinatario: {}, Asunto: {}, Plantilla: {}"),
                    eq(TO_EMAIL), eq(SUBJECT), eq(TEMPLATE_NAME)
            );
            verify(loggerMock).info(
                    eq("Correo HTML enviado exitosamente a: {}"),
                    eq(TO_EMAIL)
            );
        }
    }

    @Test
    void sendHtmlEmail_DeberiaLanzarExcepcionCuandoFalla() {
        // Arrange
        Map<String, Object> parameters = new HashMap<>();
        MessagingException expectedException = new MessagingException("Error de prueba");

        try (MockedStatic<MailUtil> mailUtilMock = mockStatic(MailUtil.class)) {
            mailUtilMock.when(() -> MailUtil.sendHtmlEmail(
                            any(), any(), any(), any()))
                    .thenThrow(expectedException);

            // Act & Assert
            RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                    mailService.sendHtmlEmail(TO_EMAIL, SUBJECT, TEMPLATE_NAME, parameters));

            assertEquals("Error al enviar correo HTML", thrown.getMessage());
            verify(loggerMock).error(
                    eq("Error al enviar correo HTML. Destinatario: {}, Plantilla: {}"),
                    eq(TO_EMAIL), eq(TEMPLATE_NAME),
                    eq(expectedException)
            );
        }
    }

    @Test
    void sendSimpleHtmlEmail_DeberiaEnviarCorreoExitosamente() {
        try (MockedStatic<MailUtil> mailUtilMock = mockStatic(MailUtil.class)) {
            // Act
            mailService.sendSimpleHtmlEmail(TO_EMAIL, SUBJECT, HTML_BODY);

            // Assert
            mailUtilMock.verify(() ->
                    MailUtil.sendSimpleHtmlEmail(TO_EMAIL, SUBJECT, HTML_BODY));

            verify(loggerMock).info(
                    eq("Iniciando envío de correo HTML simple. Destinatario: {}, Asunto: {}"),
                    eq(TO_EMAIL), eq(SUBJECT)
            );
            verify(loggerMock).info(
                    eq("Correo HTML simple enviado exitosamente a: {}"),
                    eq(TO_EMAIL)
            );
        }
    }

    @Test
    void sendSimpleHtmlEmail_DeberiaLanzarExcepcionCuandoFalla() {
        MessagingException expectedException = new MessagingException("Error de prueba");

        try (MockedStatic<MailUtil> mailUtilMock = mockStatic(MailUtil.class)) {
            mailUtilMock.when(() -> MailUtil.sendSimpleHtmlEmail(
                            any(), any(), any()))
                    .thenThrow(expectedException);

            // Act & Assert
            RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                    mailService.sendSimpleHtmlEmail(TO_EMAIL, SUBJECT, HTML_BODY));

            assertEquals("Error al enviar correo HTML simple", thrown.getMessage());
            verify(loggerMock).error(
                    eq("Error al enviar correo HTML simple. Destinatario: {}"),
                    eq(TO_EMAIL),
                    eq(expectedException)
            );
        }
    }

    @Test
    void sendEmailWithAttachment_DeberiaEnviarCorreoExitosamente() {
        try (MockedStatic<MailUtil> mailUtilMock = mockStatic(MailUtil.class)) {
            // Act
            mailService.sendEmailWithAttachment(TO_EMAIL, SUBJECT, HTML_BODY,
                    ATTACHMENT_DATA, ATTACHMENT_NAME);

            // Assert
            mailUtilMock.verify(() -> MailUtil.sendEmailWithAttachment(
                    TO_EMAIL, SUBJECT, HTML_BODY, ATTACHMENT_DATA, ATTACHMENT_NAME));

            verify(loggerMock).info(
                    eq("Iniciando envío de correo con adjunto. Destinatario: {}, Asunto: {}, Nombre del adjunto: {}"),
                    eq(TO_EMAIL), eq(SUBJECT), eq(ATTACHMENT_NAME)
            );
            verify(loggerMock).info(
                    eq("Correo con adjunto enviado exitosamente a: {}"),
                    eq(TO_EMAIL)
            );
        }
    }

    @Test
    void sendEmailWithAttachment_DeberiaLanzarExcepcionCuandoFalla() {
        MessagingException expectedException = new MessagingException("Error de prueba");

        try (MockedStatic<MailUtil> mailUtilMock = mockStatic(MailUtil.class)) {
            mailUtilMock.when(() -> MailUtil.sendEmailWithAttachment(
                            any(), any(), any(), any(), any()))
                    .thenThrow(expectedException);

            // Act & Assert
            RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                    mailService.sendEmailWithAttachment(TO_EMAIL, SUBJECT, HTML_BODY,
                            ATTACHMENT_DATA, ATTACHMENT_NAME));

            assertEquals("Error al enviar correo con adjunto", thrown.getMessage());
            verify(loggerMock).error(
                    eq("Error al enviar correo con adjunto. Destinatario: {}, Nombre del adjunto: {}"),
                    eq(TO_EMAIL), eq(ATTACHMENT_NAME),
                    eq(expectedException)
            );
        }
    }

    @Test
    void sendEmailWithCCAndBCC_DeberiaEnviarCorreoExitosamente() {
        try (MockedStatic<MailUtil> mailUtilMock = mockStatic(MailUtil.class)) {
            // Act
            mailService.sendEmailWithCCAndBCC(TO_EMAIL, CC_EMAIL, BCC_EMAIL,
                    SUBJECT, HTML_BODY);

            // Assert
            mailUtilMock.verify(() -> MailUtil.sendEmailWithCCAndBCC(
                    TO_EMAIL, CC_EMAIL, BCC_EMAIL, SUBJECT, HTML_BODY));

            verify(loggerMock).info(
                    eq("Iniciando envío de correo con CC y BCC. Destinatario: {}, CC: {}, Asunto: {}"),
                    eq(TO_EMAIL), eq(CC_EMAIL), eq(SUBJECT)
            );
            verify(loggerMock).info(
                    eq("Correo con CC y BCC enviado exitosamente a: {}"),
                    eq(TO_EMAIL)
            );
        }
    }

    @Test
    void sendEmailWithCCAndBCC_DeberiaLanzarExcepcionCuandoFalla() {
        MessagingException expectedException = new MessagingException("Error de prueba");

        try (MockedStatic<MailUtil> mailUtilMock = mockStatic(MailUtil.class)) {
            mailUtilMock.when(() -> MailUtil.sendEmailWithCCAndBCC(
                            any(), any(), any(), any(), any()))
                    .thenThrow(expectedException);

            // Act & Assert
            RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                    mailService.sendEmailWithCCAndBCC(TO_EMAIL, CC_EMAIL, BCC_EMAIL,
                            SUBJECT, HTML_BODY));

            assertEquals("Error al enviar correo con CC y BCC", thrown.getMessage());
            verify(loggerMock).error(
                    eq("Error al enviar correo con CC y BCC. Destinatario: {}, CC: {}"),
                    eq(TO_EMAIL), eq(CC_EMAIL),
                    eq(expectedException)
            );
        }
    }
}
