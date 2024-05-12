package com.example.userservice.service.Email;

import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.util.Map;

@AllArgsConstructor
@Service
public class EmailService implements IEmailService {

    private JavaMailSender javaMailSender;

    private Configuration config;

    public Boolean sendOtpMessage(String recipient, Integer otp) {
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setTo(recipient);
            helper.setSubject("OTP Verification");
            helper.setText("OTP to verify your account is: " + otp);
            javaMailSender.send(msg);
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    public Boolean sendHtmlMail(Map<String, String> model) {
        try {
            Template template = config.getTemplate("email_template.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(model.get("recipient"));
            helper.setSubject("Greetings");
            helper.setText(html, true);
            javaMailSender.send(message);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

}