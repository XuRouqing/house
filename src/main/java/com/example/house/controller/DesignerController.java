package com.example.house.controller;

import com.example.house.pojo.*;
import com.example.house.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@org.springframework.stereotype.Controller
@RequestMapping("/designer")
public class DesignerController {

    @Autowired
    private DesignerService designerService;

    @Autowired
    private HouseService houseService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomPicService roomPicService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private CityService cityService;


    @RequestMapping("/index")
    public String toIndex(HttpSession session, Model model) {
        //获取当前userId以获得当前设计师信息
//        int userId = Integer.parseInt(session.getAttribute("id").toString());
//        Designer designer = designerService.getDesignerByUserId(userId);
        Designer designer = designerService.getDesignerByUserId(3);//测试用
        int id = designer.getId();
        int houseNum = designerService.getHouseNumByDesignerId(id);
        int orderNum = designerService.getOrderNumByDesignerId(id);
        List<Appointment> appointments = appointmentService.getAppointmentByDesignerId(id);
        int appointmentNum = appointments.size();
        model.addAttribute("houseNum",houseNum);
        model.addAttribute("orderNum",orderNum);
        model.addAttribute("appointments",appointments);
        model.addAttribute("appointmentNum",appointmentNum);
        return "Designer/index";
    }

    @ResponseBody
    @RequestMapping("/changeAppointmentStatus")
    public String changeAppointmentStatus(Model model, int id, int status){
        try {
            appointmentService.updateAppointmentStatus(id,status);
            return "success";
        }catch (Exception e){
            return e.getMessage();
        }
    }

    @RequestMapping("/appointmentList")
    public String toappointmentList(HttpSession session, Model model) {
        //获取当前userId以获得当前设计师信息
//        int userId = Integer.parseInt(session.getAttribute("id").toString());
//        Designer designer = designerService.getDesignerByUserId(userId);
        Designer designer = designerService.getDesignerByUserId(3);//测试用
        int id = designer.getId();
        List<Appointment> appointments = appointmentService.getAppointmentAllByDesignerId(id);
        model.addAttribute("appointments",appointments);
        return "Designer/appointment-list";
    }

    @ResponseBody
    @RequestMapping("/delAppointment")
    public String delAppointment(Model model, int id){
        try {
            appointmentService.deleteAppointment(id);
            return "success";
        }catch (Exception e){
            return e.getMessage();
        }
    }

    @RequestMapping("/account")
    public String toAccount(HttpSession session, Model model) {
        //获取当前userId以获得当前设计师信息
//        int userId = Integer.parseInt(session.getAttribute("id").toString());
//        Designer designer = designerService.getDesignerByUserId(userId);
        Designer designer = designerService.getDesignerByUserId(3);//测试用
        int id = designer.getId();
        String styleValue = designer.getStyleValue();
        List<Index> styleIndex = designerService.getDesignerStyle();
        List<Index> designerLevel = designerService.getDesignerLevel();
        String[] styleList = styleValue.split(",");
        List<Integer> styleListInt = new ArrayList<>();
        for (int i = 0; i < styleList.length; i++) {
            styleListInt.add(Integer.parseInt(styleList[i]));
        }
        List<City> provinces = cityService.getProvinceList();
        City cityNow = cityService.getCityListById(Integer.parseInt(designer.getLocation()));
        City provinceNow = cityService.getCityListById(cityNow.getPid());
        model.addAttribute("designer",designer);
        model.addAttribute("styleIndex",styleIndex);
        model.addAttribute("styleList",styleListInt);
        model.addAttribute("designerLevel",designerLevel);
        model.addAttribute("provinces",provinces);
        model.addAttribute("cityNow",cityNow);
        model.addAttribute("provinceNow",provinceNow);
        return "Designer/account";
    }

    @Value("${user.file.path}")
    private String filePath;

    @PostMapping("/modifyAccount")
    public String modifyDesigner(Model model, Designer designer,
                                  @RequestParam("filePic") MultipartFile multipartFile){
        if (!multipartFile.isEmpty()){
            SimpleDateFormat sdf = null;
            String pic = "";
            try{
                sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                String timeStamp = sdf.format(new Date());
                String fileName = multipartFile.getOriginalFilename();//获取文件名称
                String suffixName=fileName.substring(fileName.lastIndexOf("."));
                File file = new File(filePath+timeStamp+suffixName);
                pic = "/userPic/"+timeStamp+suffixName;
                multipartFile.transferTo(file);
            }catch (IOException e){
                e.printStackTrace();
            }
            designer.setPic(pic);
        }
        designerService.modifyDesignerALL(designer);
        return  "redirect:/designer/account";
    }
}
