package com.example.house.controller;

import com.example.house.pojo.*;
import com.example.house.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
import java.util.stream.Collectors;

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

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private BookService bookService;

    @Autowired
    private WorkerService workerService;

    @Autowired
    private JavaMailSender mailSender;
    @Value("${mail.fromMail.addr}")
    private String from;

    @Autowired
    JavaMailSender javaMailSender;

    @RequestMapping("/index")
    public String toIndex(HttpSession session, Model model) {
        //获取当前userId以获得当前设计师信息
        int userId = Integer.parseInt(session.getAttribute("id").toString());
        Designer designer = designerService.getDesignerByUserId(userId);
//        Designer designer = designerService.getDesignerByUserId(3);//测试用
        int id = designer.getId();
        int houseNum = designerService.getHouseNumByDesignerId(id);
        int orderNum = designerService.getOrderNumByDesignerId(id);
        List<Appointment> appointments = appointmentService.getAppointmentByDesignerId(id);
        int appointmentNum = appointments.size();
        //更新预约订单状态
        appointmentService.updateAppointmentStatusEveryday();

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
        int userId = Integer.parseInt(session.getAttribute("id").toString());
        Designer designer = designerService.getDesignerByUserId(userId);
//        Designer designer = designerService.getDesignerByUserId(3);//测试用
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
        int userId = Integer.parseInt(session.getAttribute("id").toString());
        Designer designer = designerService.getDesignerByUserId(userId);
//        Designer designer = designerService.getDesignerByUserId(3);//测试用
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
    public String caseList(HttpSession session, Model model) {
        int userId = Integer.parseInt(session.getAttribute("id").toString());
        Designer designer = designerService.getDesignerByUserId(userId);
//        Designer designer = designerService.getDesignerByUserId(3);//测试用
        int id = designer.getId();
        List<House> houses = houseService.findHouseByDesignerId(id);
        model.addAttribute("houses",houses);
        return "Designer/caseList";
    }

    @RequestMapping("/addCase")
    public String addCase(HttpSession session, Model model) {
        int userId = Integer.parseInt(session.getAttribute("id").toString());
        Designer designer = designerService.getDesignerByUserId(userId);
//        Designer designer = designerService.getDesignerByUserId(3);//测试用
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

    @ResponseBody
    @RequestMapping("/delCase")
    public String delCae(Model model, int id){
        try {
            houseService.deleteHouse(id);
            return "success";
        }catch (Exception e){
            return e.getMessage();
        }
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
    public String saveCase(HttpSession session, HttpServletResponse resp, HttpServletRequest request) throws IOException {
        int userId = Integer.parseInt(session.getAttribute("id").toString());
        Designer designer = designerService.getDesignerByUserId(userId);
//        Designer designer = designerService.getDesignerByUserId(3);//测试用
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

    @RequestMapping("/timing")
    public String timing(HttpSession session, Model model) {
        int userId = Integer.parseInt(session.getAttribute("id").toString());
        Designer designer = designerService.getDesignerByUserId(userId);
//        Designer designer = designerService.getDesignerByUserId(3);//测试用
        int id = designer.getId();
        //接下来一周的日期
        ArrayList<String> week = new ArrayList<>();
        //加下来一周的最后一天，用于对该放入一周忙碌时间的数据做判断
        String weekLastDay = "";
        //接下来一个月的日期
        ArrayList<String> month = new ArrayList<>();
        //加下来一个月的最后一天，用于对该放入一个月忙碌时间的数据做判断
        String monthLastDay = "";
        Date d = new Date();
        long dTime = d.getTime();
        long dTimesWeek[] = new long[7];//用于存放接下来一周的Date
        long dTimesMonth[] = new long[31];//用于存放接下来一个月的Date

        //写入Date数据
        for (int i = 0; i < dTimesWeek.length; i++) {
            if (i == 0) {
                dTimesWeek[i] = dTime + 86400000;
            } else {
                dTimesWeek[i] = dTimesWeek[i - 1] + 86400000;
            }
        }
        for (int i = 0; i < dTimesMonth.length; i++) {
            if (i == 0) {
                dTimesMonth[i] = dTime + 86400000;
            } else {
                dTimesMonth[i] = dTimesMonth[i - 1] + 86400000;
            }
        }
        //数据格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        //获得当天日期，用于与数据库数据进行比较，在页面读取已设置的忙碌时间
        String today = sdf.format(new Date(Long.parseLong(String.valueOf(dTime))));

        //将接下来一周的日期数据转为字符串存入week
        for (int i = 0; i < dTimesWeek.length; i++) {
            week.add(sdf.format(new Date(Long.parseLong(String.valueOf(dTimesWeek[i]))))+"AM");
            week.add(sdf.format(new Date(Long.parseLong(String.valueOf(dTimesWeek[i]))))+"PM");
            if (i==dTimesWeek.length-1){
                weekLastDay = sdf.format(new Date(Long.parseLong(String.valueOf(dTimesWeek[i]))));
            }
        }
        //将接下来一个月的日期数据转为字符串存入month
        for (int i = 0; i < dTimesMonth.length; i++) {
            month.add(sdf.format(new Date(Long.parseLong(String.valueOf(dTimesMonth[i]))))+"AM");
            month.add(sdf.format(new Date(Long.parseLong(String.valueOf(dTimesMonth[i]))))+"PM");
            if (i==dTimesMonth.length-1){
                monthLastDay = sdf.format(new Date(Long.parseLong(String.valueOf(dTimesMonth[i]))));
            }
        }
        //获取当前设计师的忙碌时间
        List<Schedule> scheduleListAll = scheduleService.getScheduleByDesignerId(id);
        //用于存储晚于当日的忙碌时间信息
        List<Schedule> scheduleList = new ArrayList<>();
        List<Schedule> scheduleListWeek = new ArrayList<>();
        List<Schedule> scheduleListMoth = new ArrayList<>();

        //获取忙碌时间的开始时间，用于比较，若结束时间早于当日日期，则不在页面显示该条数据
        for (int i = 0; i < scheduleListAll.size(); i++) {
            String endDay = scheduleListAll.get(i).getDay().substring(scheduleListAll.get(i).getDay().indexOf('-')+1);
            if (endDay.compareTo(today)>=0){
                scheduleList.add(scheduleListAll.get(i));
            }
        }

        //用于判断该忙碌时间放置在一周忙碌时间中还是月忙碌时间中
        for (int i = 0; i < scheduleList.size(); i++) {
            String startDay = scheduleList.get(i).getDay().substring(0,scheduleList.get(i).getDay().indexOf('-'));
            //如果开始时间晚于下周最后一天，则不显示在一周忙碌时间中
            if (startDay.compareTo(weekLastDay)<=0){
                scheduleListWeek.add(scheduleList.get(i));
            }
            if (startDay.compareTo(monthLastDay)<=0){
                scheduleListMoth.add(scheduleList.get(i));
            }
        }
        scheduleListWeek = scheduleListWeek.stream().sorted(Comparator.comparing(Schedule::getDay)).collect(Collectors.toList());
        scheduleListMoth = scheduleListMoth.stream().sorted(Comparator.comparing(Schedule::getDay)).collect(Collectors.toList());
        model.addAttribute("designer",designer);
        model.addAttribute("weekDate",week);
        model.addAttribute("monthDate",month);
        model.addAttribute("scheduleList",scheduleList);
        model.addAttribute("scheduleListWeek",scheduleListWeek);
        model.addAttribute("scheduleListMoth",scheduleListMoth);
        return "Designer/timing";
    }

    @PostMapping("/addSchedule")
    public String addSchedule(Model model, int designerId, String startDate, String endDate){
        Schedule schedule = new Schedule();
        schedule.setDesignerId(designerId);

        schedule.setDate(startDate+"-"+endDate);

        String startDay = startDate.substring(0,startDate.length()-2);
        String endDay = endDate.substring(0,endDate.length()-2);
        schedule.setDay(startDay+"-"+endDay);

        String startTime = startDate.substring(startDate.length()-2);
        String endTime = endDate.substring(endDate.length()-2);
        schedule.setTime(startTime+","+endTime);
        scheduleService.addSchedule(schedule);
        return  "redirect:/designer/timing";
    }

    @RequestMapping("/delSchedule/{id}")
    public String delSchedule(Model model, @PathVariable int id){
        scheduleService.deleteSchedule(id);
        return  "redirect:/designer/timing";
    }

    @RequestMapping("/bookList")
    public String bookList(HttpSession session, Model model) {
        int userId = Integer.parseInt(session.getAttribute("id").toString());
        Designer designer = designerService.getDesignerByUserId(userId);
        List<Book> books = bookService.selectBookByDesignerId(designer.getId());
        model.addAttribute("books", books);
        return "designer/book-list";
    }

    @ResponseBody
    @RequestMapping("/delBook")
    public String delBook(Model model, int id) {
        try {
            bookService.deleteBook(id);
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @ResponseBody
    @RequestMapping("/changeBookStatus")
    public String changeBookStatus(Model model, int id, int status) {
        try {
            bookService.updateBookStatus(id, status);
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @RequestMapping("/book/{id}")
    public String book(@PathVariable int id, Model model) {
        Book book = bookService.selectBookById(id);
        List<Designer> designers = designerService.getDesignerList();
        List<Index> workerType = workerService.getWorkerType();
        List<City> provinces = cityService.getProvinceList();
        List<Worker> workers = workerService.getWorkerList();
        String workerValue = book.getWorkers();
        List<Integer> workerListInt = new ArrayList<>();
        if (workerValue!=null){
            String[] workerList = workerValue.split(",");
            for (int i = 0; i < workerList.length; i++) {
                workerListInt.add(Integer.parseInt(workerList[i]));
            }
        }
        model.addAttribute("book", book);
        model.addAttribute("designers", designers);
        model.addAttribute("workerType", workerType);
        model.addAttribute("provinces", provinces);
        model.addAttribute("workers", workers);
        model.addAttribute("workerList", workerListInt);
        return "designer/book";
    }

    @RequestMapping("/modifyBook")
    public String modifyBook(Model model, Book book) {
        bookService.modifyBook(book);
        String content = "您好，您有一个订单被修改，详情请见客户端。";
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setFrom(from);
        smm.setSubject("订单修改");
        smm.setText(content);
        smm.setTo(book.getEmail());
        try {
            javaMailSender.send(smm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/designer/book/" + book.getId();
    }

    @PostMapping("/editCase")
    public String editCase(HttpServletResponse resp, HttpServletRequest request) throws IOException {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        String mainPic;
        String original;
        String plane;
        Enumeration<String> parameterNames = request.getParameterNames();
        /**
         * 封装house
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
        house.setDesignerId(Integer.parseInt(request.getParameter("designerId")));
        house.setWorkerIds(request.getParameter("workerIds"));
        house.setTypeValue(request.getParameter("typeValue"));
        house.setTitle(request.getParameter("title"));
        house.setDes(request.getParameter("describe"));
        /**
         * 房间封装
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
            }
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
                    String name = file.getName();//获取图片input的id，用于判断该图片属于哪个字段
                    String fileName = file.getOriginalFilename();//获取文件名称
                    String suffixName = fileName.substring(fileName.lastIndexOf("."));//获取文件后缀
                    File image;
                    switch (name) {
                        case "mainPic":
                            image = new File(mainPicPath + timeStamp + suffixName);//文件名字重命名,以时间戳命名
                            file.transferTo(image);//上传文件
                            mainPic = "/mainPic/" + timeStamp + suffixName;//设置mainPic字段，house.mainPic=mainPic
                            house.setMainPic(mainPic);
                            break;
                        case "original":
                            image = new File(originalPath + timeStamp + suffixName);//文件名字重命名,以时间戳命名
                            file.transferTo(image);//上传文件
                            original = "/original/" + timeStamp + suffixName;
                            house.setOriginal(original);
                            break;
                        case "plane":
                            image = new File(planePath + timeStamp + suffixName);//文件名字重命名,以时间戳命名
                            file.transferTo(image);//上传文件
                            plane = "/plane/" + timeStamp + suffixName;
                            house.setPlane(plane);
                            break;
                        default:
                            image = new File(roomPath + timeStamp + suffixName);//文件名字重命名,以时间戳命名
                            file.transferTo(image);//上传文件
                            /**
                             * roomPic封装
                             */
                            RoomPic roomPic = new RoomPic();
                            int roomInex = Integer.parseInt(name.substring(4, name.indexOf('_')));//所属房间
                            int roomId = rooms[roomInex].getRoomId();
                            String href = "/room/" + timeStamp + suffixName;
                            String des = request.getParameter(name + "_describe");//获取该图片的describe
                            roomPic.setRoomId(roomId);
                            roomPic.setHref(href);
                            roomPic.setDes(des);
                            if (request.getParameter(name + "_id")!=null){
                                roomPic.setPicId(Integer.parseInt(request.getParameter(name + "_id")));
                                roomPicService.modifyRoomPic(roomPic);
                            }else{
                                roomPicService.addRoomPic(roomPic);
                            }
                            //数据库中写入roomPic数据
                            break;
                    }
                }
            }

        }
        houseService.modifyHouse(house);
        int houseId = house.getHouseId();
        //为room添加houseid
        for (int i = 1; i <= roomNum; i++) {
            rooms[i].setHouseId(houseId);
            roomService.modifyRoom(rooms[i]);
        }
        return "redirect:/designer/case/"+house.getHouseId();
    }

    @RequestMapping("/case/{id}")
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
        List<Integer> wokerListInt = new ArrayList<>();
        List<Integer> workerTypeList = new ArrayList<>();
        if (workerValue!=null){
            String[] workerList = workerValue.split(",");
            for (int i = 0; i < workerList.length; i++) {
                if (workerList[i]!="0"){
                    try {
                        wokerListInt.add(Integer.parseInt(workerList[i]));
                    }catch (Exception e){
                    }
                }
            }
            if (workerList.length > 0){
                for (int i = 0; i < workerList.length; i++) {
                    if (Integer.parseInt(workerList[i])!=0){
                        int type = Integer.parseInt(workerService.findWorkerById(Integer.parseInt(workerList[i])).getTypeValue());
                        workerTypeList.add(type);
                    }
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
        return "Designer/case";
    }

}
