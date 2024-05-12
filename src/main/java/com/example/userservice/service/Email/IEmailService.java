package com.example.userservice.service.Email;

import java.util.Map;

public interface IEmailService {

    public Boolean sendOtpMessage(String recipient, Integer otp);

    public Boolean sendHtmlMail(Map<String, String> model);
}
