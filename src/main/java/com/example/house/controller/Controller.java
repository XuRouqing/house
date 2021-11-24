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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.text.Style;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@org.springframework.stereotype.Controller
@RequestMapping("")
public class Controller {

    @Autowired
    private DesignerService designerService;

    @RequestMapping("/index")
    public String testSession(HttpSession session, Model model) {
        List<Designer> designers=designerService.getTopNDesigner(5);
        model.addAttribute("designerInfo",designers);
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

    @RequestMapping("/designer-list1")
    public String designerlist(HttpServletRequest request,
                               @RequestParam(required = true,defaultValue = "1") Integer pageNow,
                               RedirectAttributes attr){
        String refUrl=request.getHeader("Referer").toString();
        int levelP=refUrl.indexOf("level");
        String levelT="";
        String level="";
        if(levelP!=-1){
            levelT=refUrl.substring(levelP);
            if (levelT.indexOf('&')!=-1){
                level=levelT.substring(0,levelT.indexOf('&'));
            }
            else {
                level=levelT;
            }
        }
        int styleP=refUrl.indexOf("style");
        String styleT="";
        String style="";
        if(styleP!=-1){
            styleT=refUrl.substring(styleP);
            if (styleT.indexOf('&')!=-1){
                style=styleT.substring(0,styleT.indexOf('&'));
            }
            else {
                style=styleT;
            }
        }
        if (style!=""){
            style=style.substring(6);
            attr.addAttribute("style",style);
            pageNow=1;
        }
        if (level!=""){
            level=level.substring(6);
            attr.addAttribute("level",level);
            pageNow=1;
        }
        if(pageNow==0){
            pageNow=1;
        }
        attr.addAttribute("pageNow",pageNow);
        return "redirect:/designer-list";
    }

    @RequestMapping("/designer-list")
    public String todesignerlist(Model model, @RequestParam(required = true,defaultValue = "1") Integer pageNow,
                                 @RequestParam(required = false, defaultValue = "9") Integer pageSize,
                                 @RequestParam(required = false, defaultValue = "") String style,
                                 @RequestParam(required = false, defaultValue = "") String level,
                                 HttpServletRequest request){
        String refUrl=request.getHeader("Referer").toString();
        int levelP=refUrl.indexOf("level");
        String levelT="";
        String levelRef="";
        if(levelP!=-1){
            levelT=refUrl.substring(levelP);
            if (levelT.indexOf('&')!=-1){
                levelRef=levelT.substring(0,levelT.indexOf('&'));
            }
            else {
                levelRef=levelT;
            }
        }
        int styleP=refUrl.indexOf("style");
        String styleT="";
        String styleRef="";
        if(styleP!=-1){
            styleT=refUrl.substring(styleP);
            if (styleT.indexOf('&')!=-1){
                styleRef=styleT.substring(0,styleT.indexOf('&'));
            }
            else {
                styleRef=styleT;
            }
        }
        if (styleRef!=""){
            styleRef=styleRef.substring(6);
        }
        if (levelRef!=""){
            levelRef=levelRef.substring(6);
        }
        if (!style.equals(styleRef)||!level.equals(levelRef)){
            pageNow=1;
        }
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

//    @RequestMapping("/designer")
//    public String todesigner() {
//        return "designer.html";
//    }

    @RequestMapping("/designer/{id}")
    public String todesigner(@PathVariable int id, Model model) {
        Designer designer=designerService.findDesignerById(id);
        model.addAttribute("designerInfo",designer);
        return "designer";
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
