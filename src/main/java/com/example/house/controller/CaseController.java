package com.example.house.controller;

import com.alibaba.fastjson.JSON;
import com.example.house.pojo.*;
import com.example.house.pojo.Set;
import com.example.house.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@org.springframework.stereotype.Controller
@RequestMapping("/case")
public class CaseController {

    @Autowired
    private HouseService houseService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomPicService roomPicService;

    @Autowired
    private DesignerService designerService;

    @Autowired
    private WorkerService workerService;

    @Autowired
    private CityService cityService;

    @Autowired
    private SetService setService;

    @ModelAttribute
    public void addAttributes(Model model) {
        List<Set> set = setService.getSetList();
        model.addAttribute("setInfo", set);
    }

    @RequestMapping("/{id}")
    public String toCase(@PathVariable int id, Model model) {
        House house = houseService.findHouseById(id);
        List<Room> rooms = roomService.getRoomByHouseId(id);
        Designer designer = designerService.findDesignerById(house.getDesignerId());
        List<Worker> workers = workerService.findWorkerByHouse(id);
        model.addAttribute("houseInfo", house);
        model.addAttribute("roomsInfo", rooms);
        model.addAttribute("designerInfo", designer);
        model.addAttribute("wokerInfo", workers);
        return "case-detail";
    }


    @RequestMapping("/addCase")
    public String addCase(Model model, HttpServletResponse resp) {
        List<Designer> designers = designerService.getDesignerList();
        List<Worker> workers = workerService.getWorkerList();
        List<Index> workerTypes = workerService.getWorkerType();
        List<Index> houseStyle = houseService.getHouseStyleIndex();
        List<Index> houseArea = houseService.getHouseAreaIndex();
        List<Index> houseForm = houseService.getHouseFormIndex();
        List<Index> houseType = houseService.getHouseTypeIndex();
        List<City> provinces = cityService.getProvinceList();
        model.addAttribute("designers", designers);
        model.addAttribute("workers", workers);
        model.addAttribute("workerTypes", workerTypes);
        model.addAttribute("houseStyle", houseStyle);
        model.addAttribute("houseArea", houseArea);
        model.addAttribute("houseForm", houseForm);
        model.addAttribute("houseType", houseType);
        model.addAttribute("provinces", provinces);
        return "/addCase.html";
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
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        String mainPic;
        String original;
        String plane;
        Enumeration<String> parameterNames = request.getParameterNames();
        /**
         * ??????house
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
        house.setDesignerId(Integer.parseInt(request.getParameter("designer")));
        house.setWorkerIds(request.getParameter("workerIds"));
        house.setTypeValue(request.getParameter("typeValue"));
        house.setTitle(request.getParameter("title"));
        house.setDes(request.getParameter("describe"));
        /**
         * ????????????
         */
        int roomNum = Integer.parseInt(request.getParameter("roomNum"));
        Room[] rooms = new Room[roomNum + 1];
        for (int i = 1; i <= roomNum; i++) {
            rooms[i] = new Room();
            rooms[i].setRoomType(request.getParameter("room" + i + "_type"));
            rooms[i].setSpecificType(request.getParameter("room" + i + "_specificType"));
            rooms[i].setStyle(request.getParameter("room" + i + "_style"));
            if (request.getParameter("room" + i + "_price")!=""){
                rooms[i].setPrice(Integer.parseInt(request.getParameter("room" + i + "_price")));
                roomService.addRoom(rooms[i]);
            }
        }
        if (multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            Iterator<String> iter = multiRequest.getFileNames();
            SimpleDateFormat sdf = null;
            while (iter.hasNext()) {
                sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                String timeStamp = sdf.format(new Date());
                MultipartFile file = multiRequest.getFile(iter.next());
                String name = file.getName();//????????????input???id??????????????????????????????????????????
                String fileName = file.getOriginalFilename();//??????????????????
                String suffixName = fileName.substring(fileName.lastIndexOf("."));//??????????????????
                File image;
                switch (name) {
                    case "mainPic":
                        image = new File(mainPicPath + timeStamp + suffixName);//?????????????????????,??????????????????
                        file.transferTo(image);//????????????
                        mainPic = "/mainPic/" + timeStamp + suffixName;//??????mainPic?????????house.mainPic=mainPic
                        house.setMainPic(mainPic);
                        break;
                    case "original":
                        image = new File(originalPath + timeStamp + suffixName);//?????????????????????,??????????????????
                        file.transferTo(image);//????????????
                        original = "/original/" + timeStamp + suffixName;
                        house.setOriginal(original);
                        break;
                    case "plane":
                        image = new File(planePath + timeStamp + suffixName);//?????????????????????,??????????????????
                        file.transferTo(image);//????????????
                        plane = "/plane/" + timeStamp + suffixName;
                        house.setPlane(plane);
                        break;
                    default:
                        image = new File(roomPath + timeStamp + suffixName);//?????????????????????,??????????????????
                        file.transferTo(image);//????????????
                        /**
                         * roomPic??????
                         */
                        RoomPic roomPic = new RoomPic();
                        int roomInex = Integer.parseInt(name.substring(4, name.indexOf('_')));//????????????
                        int roomId = rooms[roomInex].getRoomId();
                        String href = "/room/" + timeStamp + suffixName;
                        String des = request.getParameter(name + "_describe");//??????????????????describe
                        roomPic.setRoomId(roomId);
                        roomPic.setHref(href);
                        roomPic.setDes(des);
                        roomPicService.addRoomPic(roomPic);//??????????????????roomPic??????
                        break;
                }
            }
        }
        houseService.addHouse(house);
        int houseId = house.getHouseId();
        //???room??????houseid
        for (int i = 1; i <= roomNum; i++) {
            rooms[i].setHouseId(houseId);
            roomService.modifyRoom(rooms[i]);
        }
        return "redirect:/case/addCase";
    }

    @PostMapping("/editCase")
    public String editCase(HttpServletResponse resp, HttpServletRequest request) throws IOException {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        String mainPic;
        String original;
        String plane;
        Enumeration<String> parameterNames = request.getParameterNames();
//        while(parameterNames.hasMoreElements()){
//            String name=(String)parameterNames.nextElement();
//            String value=request.getParameter(name);
//            System.out.println(name + "=" + value);
//        }
        /**
         * ??????house
         */
        House house = houseService.findHouseById(Integer.parseInt(request.getParameter("houseId")));
        house.setStyleValue(request.getParameter("styleValue"));
        house.setHouseType(request.getParameter("houseType"));
        house.setAreaValue(request.getParameter("areaValue"));
        house.setFormValue(request.getParameter("formValue"));
        house.setCity(request.getParameter("city"));
        house.setLocal(request.getParameter("location"));
        house.setTime(request.getParameter("time"));
        house.setPrice(Integer.parseInt(request.getParameter("price")));
        house.setDesignerId(Integer.parseInt(request.getParameter("designer")));
        house.setWorkerIds(request.getParameter("workerIds"));
        house.setTypeValue(request.getParameter("typeValue"));
        house.setTitle(request.getParameter("title"));
        house.setDes(request.getParameter("describe"));
        /**
         * ????????????
         */
        int roomNum = Integer.parseInt(request.getParameter("roomNum"));
        Room[] rooms = new Room[roomNum + 1];
        for (int i = 1; i <= roomNum; i++) {
            rooms[i] = new Room();
            rooms[i].setRoomType(request.getParameter("room" + i + "_type"));
            rooms[i].setSpecificType(request.getParameter("room" + i + "_specificType"));
            rooms[i].setStyle(request.getParameter("room" + i + "_style"));
            rooms[i].setPrice(Integer.parseInt(request.getParameter("room" + i + "_price")));
            if (request.getParameter("room" + i + "_id")!=null){
                rooms[i].setRoomId(Integer.parseInt(request.getParameter("room" + i + "_id")));
                roomService.modifyRoom(rooms[i]);
            }else {
                roomService.addRoom(rooms[i]);
            }

        }
        if (multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            Iterator<String> iter = multiRequest.getFileNames();
            SimpleDateFormat sdf = null;
            while (iter.hasNext()) {
                sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                String timeStamp = sdf.format(new Date());
                MultipartFile file = multiRequest.getFile(iter.next());
                if (!file.isEmpty()) {
                    String name = file.getName();//????????????input???id??????????????????????????????????????????
                    String fileName = file.getOriginalFilename();//??????????????????
                    String suffixName = fileName.substring(fileName.lastIndexOf("."));//??????????????????
                    File image;
                    switch (name) {
                        case "mainPic":
                            image = new File(mainPicPath + timeStamp + suffixName);//?????????????????????,??????????????????
                            file.transferTo(image);//????????????
                            mainPic = "/mainPic/" + timeStamp + suffixName;//??????mainPic?????????house.mainPic=mainPic
                            house.setMainPic(mainPic);
                            break;
                        case "original":
                            image = new File(originalPath + timeStamp + suffixName);//?????????????????????,??????????????????
                            file.transferTo(image);//????????????
                            original = "/original/" + timeStamp + suffixName;
                            house.setOriginal(original);
                            break;
                        case "plane":
                            image = new File(planePath + timeStamp + suffixName);//?????????????????????,??????????????????
                            file.transferTo(image);//????????????
                            plane = "/plane/" + timeStamp + suffixName;
                            house.setPlane(plane);
                            break;
                        default:
                            image = new File(roomPath + timeStamp + suffixName);//?????????????????????,??????????????????
                            file.transferTo(image);//????????????
                            /**
                             * roomPic??????
                             */
                            RoomPic roomPic = new RoomPic();
                            int roomInex = Integer.parseInt(name.substring(4, name.indexOf('_')));//????????????
                            int roomId = rooms[roomInex].getRoomId();
                            String href = "/room/" + timeStamp + suffixName;
                            String des = request.getParameter(name + "_describe");//??????????????????describe
                            System.out.println(des);
                            roomPic.setRoomId(roomId);
                            roomPic.setHref(href);
                            roomPic.setDes(des);
                            if (request.getParameter(name + "_id")!=null){
                                roomPic.setPicId(Integer.parseInt(request.getParameter(name + "_id")));
                                roomPicService.modifyRoomPic(roomPic);
                            }else{
                                roomPicService.addRoomPic(roomPic);
                            }
                            //??????????????????roomPic??????
                            break;
                    }
                }
            }

        }
        houseService.modifyHouse(house);
        int houseId = house.getHouseId();
        //???room??????houseid
        for (int i = 1; i <= roomNum; i++) {
            rooms[i].setHouseId(houseId);
            roomService.modifyRoom(rooms[i]);
        }
        return "redirect:/case/editCase/"+house.getHouseId();
    }

    @RequestMapping("/delCase/{id}")
    public String delCase(Model model, @PathVariable int id) {
        houseService.deleteHouse(id);
        return "redirect:/fullwidth";
    }

    @RequestMapping("/editCase/{id}")
    public String editCase(@PathVariable int id, Model model) {
        List<Designer> designers = designerService.getDesignerList();
        List<Worker> workers = workerService.getWorkerList();
        List<Index> workerTypes = workerService.getWorkerType();
        List<Index> houseStyle = houseService.getHouseStyleIndex();
        List<Index> houseArea = houseService.getHouseAreaIndex();
        List<Index> houseForm = houseService.getHouseFormIndex();
        List<Index> houseType = houseService.getHouseTypeIndex();
        List<City> provinces = cityService.getProvinceList();

        House house = houseService.findHouseById(id);
        List<Room> roomList = roomService.getRoomByHouseId(id);
        int nowCity = cityService.getCityListById(Integer.parseInt(house.getCity())).getId();
        int nowProvince = cityService.getCityListById(Integer.parseInt(house.getCity())).getPid();

        List<Integer> roomPic = new ArrayList<>();
        roomPic.add(0);
        for (int i = 0; i < roomList.size(); i++) {
            List<RoomPic> roomPicList = roomPicService.getRoomPicByRoomId(roomList.get(i).getRoomId());
            roomList.get(i).setRoomPics(roomPicList);
            roomPic.add(roomService.getPicNumByRoomId(roomList.get(i).getRoomId()));
        }
        String workerValue = house.getWorkerIds();
        String[] workerList = workerValue.split(",");
        List<Integer> wokerListInt = new ArrayList<>();
        for (int i = 0; i < workerList.length; i++) {
            if (workerList[i]!="0"){
                try {
                    wokerListInt.add(Integer.parseInt(workerList[i]));
                }catch (Exception e){
                }
            }
        }
        List<Integer> workerTypeList = new ArrayList<>();
        if (workerList.length > 0){
            for (int i = 0; i < workerList.length; i++) {
                if (Integer.parseInt(workerList[i])!=0){
                    int type = Integer.parseInt(workerService.findWorkerById(Integer.parseInt(workerList[i])).getTypeValue());
                    workerTypeList.add(type);
                }
            }
        }

        model.addAttribute("designers", designers);
        model.addAttribute("workers", workers);
        model.addAttribute("workerTypes", workerTypes);
        model.addAttribute("houseStyle", houseStyle);
        model.addAttribute("houseArea", houseArea);
        model.addAttribute("houseForm", houseForm);
        model.addAttribute("houseType", houseType);
        model.addAttribute("provinces", provinces);
        model.addAttribute("house", house);
        model.addAttribute("roomList", roomList);
        model.addAttribute("roomNum", roomList.size());
        model.addAttribute("roomPic", roomPic);
        model.addAttribute("nowCity", nowCity);
        model.addAttribute("nowProvince", nowProvince);
        model.addAttribute("workerList", wokerListInt);
        model.addAttribute("workerTypeList", workerTypeList);
        return "editCase";
    }


}
