package com.example.house.controller;

import com.example.house.service.MailService;
import com.example.house.service.MailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

public class Email {

    @Autowired
    private MailService mailService;

    MailServiceImpl mail = new MailServiceImpl();

    @Value("${mail.fromMail.addr}")

    public void sendEmail(String email,String theme,String content){
        mail.sendSimpleMail(email,theme,content);
    }
}
