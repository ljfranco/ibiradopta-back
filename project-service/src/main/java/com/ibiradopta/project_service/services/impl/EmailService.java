package com.ibiradopta.project_service.services.impl;

import com.ibiradopta.project_service.models.dto.EmailDetailsDto;
import com.ibiradopta.project_service.services.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService implements IEmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine; //motor de plantillas Thymeleaf

    @Value("${spring.mail.username}")
    private String emailSender;

    Logger logger = LoggerFactory.getLogger(this.getClass());


    public void sendEmail(EmailDetailsDto emailDetails) {
        try {
            // Crear el contexto de Thymeleaf
            Context context = new Context();
            context.setVariable("userName", emailDetails.getUserName());
            context.setVariable("projectName", emailDetails.getProjectName());
            context.setVariable("paymentDate", emailDetails.getPaymentDate());
            context.setVariable("quantity", emailDetails.getQuantity());
            context.setVariable("amount", emailDetails.getAmount());


            // Procesar la plantilla con el contexto
            String body = templateEngine.process("donation_confirmation_email", context);


            // Crear el mensaje de correo con contenido HTML
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true); // true indica que el mensaje es HTML
            helper.setTo(emailDetails.getRecipient());
            helper.setFrom(emailSender);
            helper.setSubject(emailDetails.getSubject());
            helper.setText(body, true); // 'true' indica que es un correo en formato HTML

            // Enviar el correo
            javaMailSender.send(mimeMessage);
            logger.info("Email successfully sent to: {}", emailDetails.getRecipient());
        } catch (MailException | MessagingException e) {
            logger.error("Error sending email to: {}", emailDetails.getRecipient(), e);
        }
    }
}
