package com.example.userservice.service;

import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import freemarker.template.Configuration;
import jakarta.mail.internet.MimeMessage;

import java.util.Arrays;
import java.util.Map;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private Configuration config;

    public Boolean sendOtpMessage(String to, Integer otp) {
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setTo(to);
            helper.setSubject("OTP Verification");
            helper.setText("OTP to verify your account is: " + otp);
            javaMailSender.send(msg);
            return true;
        }
        catch(Exception e){
            System.out.println(e.getMessage());
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
            System.out.println(e.getMessage());
            return false;
        }
    }

}