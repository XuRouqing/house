package com.example.house.controller;

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
        model.addAttribute("setInfo",set);
    }

    @RequestMapping("/{id}")
    public String toCase(@PathVariable int id, Model model) {
        House house = houseService.findHouseById(id);
        List<Room> rooms = roomService.getRoomByHouseId(id);
        Designer designer = designerService.findDesignerById(house.getDesignerId());
        List<Worker> workers = workerService.findWorkerByHouse(id);
        model.addAttribute("houseInfo",house);
        model.addAttribute("roomsInfo",rooms);
        model.addAttribute("designerInfo",designer);
        model.addAttribute("wokerInfo",workers);
        return "case-detail";
    }


    @RequestMapping("/addCase")
    public String addCase(Model model, HttpServletResponse resp){
        List<Designer> designers = designerService.getDesignerList();
        List<Worker> workers = workerService.getWorkerList();
        List<Index> workerTypes = workerService.getWorkerType();
        List<Index> houseStyle = houseService.getHouseStyleIndex();
        List<Index> houseArea = houseService.getHouseAreaIndex();
        List<Index> houseForm = houseService.getHouseFormIndex();
        List<Index> houseType = houseService.getHouseTypeIndex();
        List<City> provinces = cityService.getProvinceList();
        model.addAttribute("designers",designers);
        model.addAttribute("workers",workers);
        model.addAttribute("workerTypes",workerTypes);
        model.addAttribute("houseStyle",houseStyle);
        model.addAttribute("houseArea",houseArea);
        model.addAttribute("houseForm",houseForm);
        model.addAttribute("houseType",houseType);
        model.addAttribute("provinces",provinces);
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
        house.setPrice(Double.parseDouble(request.getParameter("price")));
        house.setDesignerId(Integer.parseInt(request.getParameter("designer")));
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
        return "redirect:/case/addCase";
    }

    @ResponseBody
    @PostMapping("/delCase")
    public String delCase(Model model, int id){
        try {
            houseService.deleteHouse(id);
            return "success";
        }catch (Exception e){
            return e.getMessage();
        }
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

        for (int i = 0; i < roomList.size(); i++) {
            List<RoomPic> roomPicList = roomPicService.getRoomPicByRoomId(roomList.get(i).getRoomId());
            roomList.get(i).setRoomPics(roomPicList);
        }
        String workerValue = house.getWorkerIds();
        String[] workerList = workerValue.split(",");
        List<Integer> wokerListInt = new ArrayList<>();
        for (int i = 0; i < workerList.length; i++) {
            wokerListInt.add(Integer.parseInt(workerList[i]));
        }
        List<Integer> workerTypeList = new ArrayList<>();
        for (int i = 0; i < workerList.length; i++) {
            int type = Integer.parseInt(workerService.findWorkerById(Integer.parseInt(workerList[i])).getTypeValue());
            workerTypeList.add(type);
        }

        model.addAttribute("designers",designers);
        model.addAttribute("workers",workers);
        model.addAttribute("workerTypes",workerTypes);
        model.addAttribute("houseStyle",houseStyle);
        model.addAttribute("houseArea",houseArea);
        model.addAttribute("houseForm",houseForm);
        model.addAttribute("houseType",houseType);
        model.addAttribute("provinces",provinces);
        model.addAttribute("house",house);
        model.addAttribute("roomList",roomList);
        model.addAttribute("nowCity",nowCity);
        model.addAttribute("nowProvince",nowProvince);
        model.addAttribute("workerList",wokerListInt);
        model.addAttribute("workerTypeList",workerTypeList);
        return "editCase";
    }


}
