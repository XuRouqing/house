package com.example.house.controller;


import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@org.springframework.stereotype.Controller
@RequestMapping("")
public class Controller {
    @RequestMapping("/index")
    public String testSession(HttpSession session) {
        return "index";
    }

    @RequestMapping("/account")
    public String toAccount() {
        return "my-account";
    }

    @RequestMapping("/fullwidth")
    public String tofullwidth() {
        return "shop-grid-fullwidth";
    }

    @RequestMapping("/designer-list")
    public String todesignerlist() {
        return "designer-list.html";
    }

    @RequestMapping("/worker-list")
    public String toworkderlist() {
        return "worker-list.html";
    }

    @RequestMapping("/discount")
    public String todiscount() {
        return "discount.html";
    }

    @RequestMapping("/calendar")
    public String tocalendar() {
        return "calendar.html";
    }

    @RequestMapping("/contact")
    public String tocontact() {
        return "contact.html";
    }

    @RequestMapping("/login-register")
    public String tologin() {
        return "login-register.html";
    }

    @RequestMapping("/booking")
    public String tobooking() {
        return "booking.html";
    }
}
