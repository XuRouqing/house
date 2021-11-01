package com.example.house.controller;


import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@org.springframework.stereotype.Controller
@RequestMapping("/")
public class Controller {
    @RequestMapping("/index")
    public String testSession(HttpSession session) {
        return "idnex";
    }

}
