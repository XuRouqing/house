package com.example.house.controller;


import com.example.house.pojo.Designer;
import com.example.house.service.DesignerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@org.springframework.stereotype.Controller
@RequestMapping("")
public class Controller {

    @Autowired
    private DesignerService designerService;

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
    public String todesignerlist(Model model, @RequestParam(required = true,defaultValue = "1") Integer pageNow,
                                 @RequestParam(required = false, defaultValue = "8") Integer pageSize,
                                 @RequestParam(required = false, defaultValue = "") String style,
                                 @RequestParam(required = false, defaultValue = "") String level ){
        PageHelper.startPage(pageNow,5);
        List<Designer> designers=designerService.getDesignerByPageAndSL(pageNow,pageSize,style,level);
        PageInfo<Designer> designerPageInfo=new PageInfo<>(designers);
        model.addAttribute("pageInfo",designerPageInfo);
        model.addAttribute("designerInfo",designers);
        return "designer-list";
    }

//    @RequestMapping("/designer-list")
//    public String todesignerlist(Model model, @RequestParam(required = true,defaultValue = "1") Integer pageNow,
//                                 @RequestParam(required = false,defaultValue = "8") Integer pageSize, HttpServletRequest request){
//        PageHelper.startPage(pageNow,5);
//        List<Designer> designers=designerService.getDesignerListByPage(pageNow,pageSize);
//        PageInfo<Designer> designerPageInfo=new PageInfo<>(designers);
//        model.addAttribute("pageInfo",designerPageInfo);
//        model.addAttribute("designerInfo",designers);
//        return "designer-list";
//    }

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
