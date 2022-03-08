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

    @RequestMapping("/caseList")
    public String caseList(Model model) {
//        int userId = Integer.parseInt(session.getAttribute("id").toString());
//        Designer designer = designerService.getDesignerByUserId(userId);
        Designer designer = designerService.getDesignerByUserId(3);//测试用
        int id = designer.getId();
        List<House> houses = houseService.findHouseByDesignerId(id);
        model.addAttribute("houses",houses);
        return "Designer/caseList";
    }

    @RequestMapping("/addCase")
    public String addCase(Model model) {
//        int userId = Integer.parseInt(session.getAttribute("id").toString());
//        Designer designer = designerService.getDesignerByUserId(userId);
        Designer designer = designerService.getDesignerByUserId(3);//测试用
        int id = designer.getId();
        List<Index> houseStyle = houseService.getHouseStyleIndex();
        List<Index> houseArea = houseService.getHouseAreaIndex();
        List<Index> houseForm = houseService.getHouseFormIndex();
        List<Index> houseType = houseService.getHouseTypeIndex();
        List<City> provinces = cityService.getProvinceList();
        model.addAttribute("houseStyle",houseStyle);
        model.addAttribute("houseArea",houseArea);
        model.addAttribute("houseForm",houseForm);
        model.addAttribute("houseType",houseType);
        model.addAttribute("provinces",provinces);
        return "Designer/addCase";
    }

    @Value("${room.file.path}")
    private String roomPath;
    @Value("${mainPic.file.path}")
    private String mainPicPath;
    @Value("${original.file.path}")
    private String originalPath;
    @Value("${plane.file.path}")
    private String planePath;

    @PostMapping("/saveCase")
    public String saveCase(HttpServletResponse resp, HttpServletRequest request) throws IOException {
        //        int userId = Integer.parseInt(session.getAttribute("id").toString());
//        Designer designer = designerService.getDesignerByUserId(userId);
        Designer designer = designerService.getDesignerByUserId(3);//测试用
        int id = designer.getId();
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        String mainPic;
        String original;
        String plane;
        Enumeration<String> parameterNames = request.getParameterNames();
        /**
         * 封装house
         */
        House house = new House();
        house.setStyleValue(request.getParameter("styleValue"));
        house.setHouseType(request.getParameter("houseType"));
        house.setAreaValue(request.getParameter("areaValue"));
        house.setFormValue(request.getParameter("formValue"));
        house.setCity(request.getParameter("city"));
        house.setLocal(request.getParameter("location"));
        house.setTime(request.getParameter("time"));
        house.setPrice(Integer.parseInt(request.getParameter("price")));
        house.setDesignerId(id);
        house.setWorkerIds(request.getParameter("workerIds"));
        house.setTypeValue(request.getParameter("typeValue"));
        house.setTitle(request.getParameter("title"));
        house.setDes(request.getParameter("describe"));
        /**
         * 房间封装
         */
        int roomNum = Integer.parseInt(request.getParameter("roomNum"));
        Room[] rooms = new Room[roomNum+1];
        for (int i = 1; i <= roomNum;i++){
            rooms[i] = new Room();
            rooms[i].setRoomType(request.getParameter("room"+i+"_type"));
            rooms[i].setSpecificType(request.getParameter("room"+i+"_specificType"));
            rooms[i].setStyle(request.getParameter("room"+i+"_style"));
            rooms[i].setPrice(Integer.parseInt(request.getParameter("room"+i+"_price")));
            roomService.addRoom(rooms[i]);
        }
        if(multipartResolver.isMultipart(request)){
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            Iterator<String> iter = multiRequest.getFileNames();
            SimpleDateFormat sdf = null;
            while(iter.hasNext()){
                sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                String timeStamp = sdf.format(new Date());
                MultipartFile file = multiRequest.getFile(iter.next());
                String name = file.getName();//获取图片input的id，用于判断该图片属于哪个字段
                String fileName = file.getOriginalFilename();//获取文件名称
                String suffixName=fileName.substring(fileName.lastIndexOf("."));//获取文件后缀
                File image;
                switch (name){
                    case "mainPic":
                        image = new File(mainPicPath+timeStamp+suffixName);//文件名字重命名,以时间戳命名
                        file.transferTo(image);//上传文件
                        mainPic = "/mainPic/"+timeStamp+suffixName;//设置mainPic字段，house.mainPic=mainPic
                        house.setMainPic(mainPic);
                        break;
                    case "original":
                        image = new File(originalPath+timeStamp+suffixName);//文件名字重命名,以时间戳命名
                        file.transferTo(image);//上传文件
                        original = "/original/"+timeStamp+suffixName;
                        house.setOriginal(original);
                        break;
                    case "plane":
                        image = new File(planePath+timeStamp+suffixName);//文件名字重命名,以时间戳命名
                        file.transferTo(image);//上传文件
                        plane = "/plane/"+timeStamp+suffixName;
                        house.setPlane(plane);
                        break;
                    default:
                        image = new File(roomPath+timeStamp+suffixName);//文件名字重命名,以时间戳命名
                        file.transferTo(image);//上传文件
                        /**
                         * roomPic封装
                         */
                        RoomPic roomPic = new RoomPic();
                        int roomInex = Integer.parseInt(name.substring(4, name.indexOf('_')));//所属房间
                        int roomId = rooms[roomInex].getRoomId();
                        String href = "/room/"+timeStamp+suffixName;
                        String des = request.getParameter(name+"_describe");//获取该图片的describe
                        roomPic.setRoomId(roomId);
                        roomPic.setHref(href);
                        roomPic.setDes(des);
                        roomPicService.addRoomPic(roomPic);//数据库中写入roomPic数据
                        break;
                }
            }
        }
        houseService.addHouse(house);
        int houseId = house.getHouseId();
        //为room添加houseid
        for (int i=1;i<=roomNum;i++){
            rooms[i].setHouseId(houseId);
            roomService.modifyRoom(rooms[i]);
        }
        return "redirect:/designer/addCase";
    }
}
