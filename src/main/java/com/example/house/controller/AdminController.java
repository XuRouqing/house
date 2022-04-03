package com.example.house.controller;

import com.example.house.pojo.*;
import com.example.house.service.*;
import com.github.pagehelper.PageInfo;
import com.sun.corba.se.spi.orbutil.threadpool.Work;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.security.krb5.internal.crypto.Des;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private HouseService houseService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private DesignerService designerService;

    @Autowired
    private WorkerService workerService;

    @Autowired
    private UserService userService;

    @Autowired
    private CityService cityService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private SetOrderService setOrderService;

    @Autowired
    private IndexService indexService;

    @Autowired
    private BookService bookService;

    @Autowired
    private JavaMailSender mailSender;
    @Value("${mail.fromMail.addr}")
    private String from;

    @Autowired
    JavaMailSender javaMailSender;

    @RequestMapping("/index")
    public String toindex(Model model) {
        int workerNum = workerService.getWorkerNum();
        int designerNum = designerService.getDesignerNum();
        List<Designer> designers = designerService.getDesignerList();
        List<Worker> workers = workerService.getWorkerList();
        int appointmentNum = appointmentService.getAppointmentNum();
        int setOrderNum = setOrderService.getSetOrderNum();
        int orderNum = appointmentNum + setOrderNum;
        int userNum = userService.getUserNum();
        model.addAttribute("workerNum", workerNum);
        model.addAttribute("designerNum", designerNum);
        model.addAttribute("workers", workers);
        model.addAttribute("designers", designers);
        model.addAttribute("orderNum", orderNum);
        model.addAttribute("userNum", userNum);
        return "Admin/index";
    }

    @ResponseBody
    @RequestMapping("/getdesignerList")
    public List<Designer> getDesignerList(@RequestParam("pageNow") int pageNow,
                                          @RequestParam("pageCount") int pageCount) {
        List<Designer> designerList = designerService.getDesignerListByPage(pageNow, pageCount);
        return designerList;
    }

    @ResponseBody
    @RequestMapping("/getworkerList")
    public List<Worker> getWorkerList(@RequestParam("pageNow") int pageNow,
                                      @RequestParam("pageCount") int pageCount) {
        List<Worker> workerList = workerService.getWorkerListByPage(pageNow, pageCount);
        return workerList;
    }

    @RequestMapping("/designerList")
    public String todesignerList(Model model) {
        List<Designer> designers = designerService.getDesignerList();
        List<Index> designerLevel = designerService.getDesignerLevel();
        List<Index> designerStyle = designerService.getDesignerStyle();
        List<City> provinces = cityService.getProvinceList();
        model.addAttribute("designers", designers);
        model.addAttribute("designerLevel", designerLevel);
        model.addAttribute("designerStyle", designerStyle);
        model.addAttribute("provinces", provinces);
        return "Admin/designer-list";
    }

    @RequestMapping("/designer/{id}")
    public String todesigner(Model model,
                             @PathVariable int id) {
        Designer designer = designerService.findDesignerById(id);
        List<Index> designerLevel = designerService.getDesignerLevel();
        List<Index> designerStyle = designerService.getDesignerStyle();
        //获取设计师的风格列表并将其转为数组
        String styleValue = designer.getStyleValue();
        String[] styleList = styleValue.split(",");
        List<Integer> styleListInt = new ArrayList<>();
        for (int i = 0; i < styleList.length; i++) {
            styleListInt.add(Integer.parseInt(styleList[i]));
        }
        List<City> provinces = cityService.getProvinceList();
        City cityNow = cityService.getCityListById(Integer.parseInt(designer.getLocation()));
        City provinceNow = cityService.getCityListById(cityNow.getPid());
        model.addAttribute("designer", designer);
        model.addAttribute("designerLevel", designerLevel);
        model.addAttribute("designerStyle", designerStyle);
        model.addAttribute("styleList", styleListInt);
        model.addAttribute("provinces", provinces);
        model.addAttribute("cityNow", cityNow);
        model.addAttribute("provinceNow", provinceNow);
        return "Admin/designer";
    }

    @Value("${user.file.path}")
    private String filePath;

    @RequestMapping("/modifyDesigner1")
    public String modifyDesigner1(Model model, Designer designer,
                                  @RequestParam("filePic") MultipartFile multipartFile) {
        if (!multipartFile.isEmpty()) {
            SimpleDateFormat sdf = null;
            String pic = "";
            try {
                sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                String timeStamp = sdf.format(new Date());
                String fileName = multipartFile.getOriginalFilename();//获取文件名称
                String suffixName = fileName.substring(fileName.lastIndexOf("."));
                File file = new File(filePath + timeStamp + suffixName);
                pic = "/userPic/" + timeStamp + suffixName;
                multipartFile.transferTo(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            designer.setPic(pic);
        }
        designerService.modifyDesignerMain(designer);
        String content = "设计师" + designer.getName() + "您好," + "您的信息已被管理员修改，详情请于客户端查看。";
        SimpleMailMessage smmToAdmin = new SimpleMailMessage();
        smmToAdmin.setFrom(from);
        smmToAdmin.setSubject("设计师个人信息修改");
        smmToAdmin.setText(content);
        smmToAdmin.setTo(from);
        try {
            javaMailSender.send(smmToAdmin);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/admin/designer/" + designer.getId();
    }

    @RequestMapping("/modifyDesigner2")
    public String modifyDesigner2(Model model, Designer designer) {
        designerService.modifyDesigner(designer);
        String content = "设计师" + designer.getName() + "您好," + "您的信息已被管理员修改，详情请于客户端查看。";
        SimpleMailMessage smmToAdmin = new SimpleMailMessage();
        smmToAdmin.setFrom(from);
        smmToAdmin.setSubject("新的预约订单");
        smmToAdmin.setText(content);
        smmToAdmin.setTo(from);
        try {
            javaMailSender.send(smmToAdmin);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/admin/designer/" + designer.getId();
    }

    @RequestMapping("/addDesigner")
    public String addDesigner(Model model, Designer designer,
                              @RequestParam("filePic") MultipartFile multipartFile) {
        if (!multipartFile.isEmpty()) {
            SimpleDateFormat sdf = null;
            String pic = "";
            try {
                sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                String timeStamp = sdf.format(new Date());
                String fileName = multipartFile.getOriginalFilename();//获取文件名称
                String suffixName = fileName.substring(fileName.lastIndexOf("."));
                File file = new File(filePath + timeStamp + suffixName);
                pic = "/userPic/" + timeStamp + suffixName;
                multipartFile.transferTo(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //新建设计师的时候自动创建user，作为设计师的账号
            User user = new User();
            user.setCode(designer.getTel());
            user.setName(designer.getName());
            user.setRole("designer");
            user.setPassword("123");
            user.setEmail(designer.getEmail());
            user.setPhone(designer.getTel());
            userService.addUser(user);
            designer.setPic(pic);
            designer.setUserId(user.getId());
            designerService.addDesigner(designer);
        }
        return "redirect:/admin/designerList";
    }

    @ResponseBody
    @RequestMapping("/delDesigner")
    public String delDesigner(Model model, int id) {
        try {
            designerService.deleteDesigner(id);
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Value("${worker.file.path}")
    private String workerFilePath;

    @RequestMapping("/workerList")
    public String toworkerList(Model model) {
        List<Worker> workers = workerService.getWorkerList();
        List<Index> workerType = workerService.getWorkerType();
        List<City> provinces = cityService.getProvinceList();
        model.addAttribute("workers", workers);
        model.addAttribute("workerType", workerType);
        model.addAttribute("provinces", provinces);
        return "Admin/worker-list";
    }

    @RequestMapping("/worker/{id}")
    public String toworker(@PathVariable int id, Model model) {
        Worker worker = workerService.findWorkerById(id);
        List<Index> workerType = workerService.getWorkerType();
        List<City> provinces = cityService.getProvinceList();
        City cityNow = cityService.getCityListById(worker.getCityId());
        City provinceNow = cityService.getCityListById(cityNow.getPid());
        model.addAttribute("workerInfo", worker);
        model.addAttribute("workerType", workerType);
        model.addAttribute("provinces", provinces);
        model.addAttribute("cityNow", cityNow);
        model.addAttribute("provinceNow", provinceNow);
        return "Admin/worker";
    }

    @RequestMapping("/modifyWorker1")
    public String modifyWorker1(Model model, Worker worker,
                                @RequestParam("filePic") MultipartFile multipartFile) {
        if (!multipartFile.isEmpty()) {
            SimpleDateFormat sdf = null;
            String pic = "";
            try {
                sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                String timeStamp = sdf.format(new Date());
                String fileName = multipartFile.getOriginalFilename();//获取文件名称
                String suffixName = fileName.substring(fileName.lastIndexOf("."));
                File file = new File(filePath + timeStamp + suffixName);
                pic = "/userPic/" + timeStamp + suffixName;
                multipartFile.transferTo(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            worker.setPic(pic);
        }
        workerService.modifyWorkerMain(worker);
        return "redirect:/admin/worker/" + worker.getId();
    }

    @RequestMapping("/modifyWorker2")
    public String modifyWorker2(Model model, Worker worker) {
        workerService.modifyWorker(worker);
        return "redirect:/admin/worker/" + worker.getId();
    }

    @RequestMapping("/addWorker")
    public String addWorker(Model model, Worker worker,
                            @RequestParam("filePic") MultipartFile multipartFile) {
        if (!multipartFile.isEmpty()) {
            SimpleDateFormat sdf = null;
            String pic = "";
            try {
                sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                String timeStamp = sdf.format(new Date());
                String fileName = multipartFile.getOriginalFilename();//获取文件名称
                String suffixName = fileName.substring(fileName.lastIndexOf("."));
                File file = new File(workerFilePath + timeStamp + suffixName);
                pic = "/workerPic/" + timeStamp + suffixName;
                multipartFile.transferTo(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            worker.setPic(pic);
        }
        workerService.addWorker(worker);
        return "redirect:/admin/workerList";
    }

    @ResponseBody
    @RequestMapping("/delWorker")
    public String delWorker(Model model, int id) {
        try {
            workerService.deleteWorker(id);
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @RequestMapping("/appointmentList")
    public String toappointmentList(Model model) {
        List<Appointment> appointments = appointmentService.getAppointmentList();
        model.addAttribute("appointments", appointments);
//        appointmentService.updateAppointmentStatusEveryday();
        return "Admin/appointment-list";
    }

    @ResponseBody
    @RequestMapping("/changeAppointmentStatus")
    public String changeAppointmentStatus(Model model, int id, int status) {
        try {
            appointmentService.updateAppointmentStatus(id, status);
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @ResponseBody
    @RequestMapping("/delAppointment")
    public String delAppointment(Model model, int id) {
        try {
            appointmentService.deleteAppointment(id);
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @RequestMapping("/setOrderList")
    public String setOrderList(Model model) {
        List<SetOrder> setOrderList = setOrderService.getSetOrderAll();
        model.addAttribute("setOrderList", setOrderList);
        return "Admin/setOrder-list";
    }

    @ResponseBody
    @RequestMapping("/changeSetOrderStatus")
    public String changeSetOrderStatus(Model model, int id, int status) {
        try {
            setOrderService.updateSetOrderStatus(id, status);
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @ResponseBody
    @RequestMapping("/delSetOrder")
    public String delSetOrder(Model model, int id) {
        try {
            setOrderService.delSetOrder(id);
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @RequestMapping("/userList")
    public String userList(Model model) {
        List<User> users = userService.getUserList();
        model.addAttribute("users", users);
        return "Admin/user-list";
    }

    @RequestMapping("/addUser")
    public String addUser(Model model, User user) {
        userService.addUser(user);
        return "redirect:/admin/userList";
    }

    @ResponseBody
    @RequestMapping("/delUser")
    public String delUser(Model model, int id) {
        try {
            userService.deleteUser(id);
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @ResponseBody
    @RequestMapping("/checkCode")
    public String checkCode(Model model, String code) {
        int flag = userService.checkCode(code);
        if (flag == 0){
            return "true";
        }
        return "false";
    }

    @RequestMapping("/modifyUser")
    public String modifyUser(Model model, User user) {
        userService.modifyUser(user);
        return "redirect:/admin/userList";
    }

    @RequestMapping("/indexList/{index}")
    public String toindexList(Model model, @PathVariable String index) {
        List<Index> indexList = new ArrayList<>();
        switch (index){
            case "housearea":
                indexList = houseService.getHouseAreaIndex();
                break;
            case "houseform":
                indexList = houseService.getHouseFormIndex();
                break;
            case "housetype":
                indexList = houseService.getHouseTypeIndex();
                break;
            case "styleindex":
                indexList = houseService.getHouseStyleIndex();
                break;
            case "workertype":
                indexList = workerService.getWorkerType();
                break;
        }
        model.addAttribute(indexList);
        return "Admin/indexList";
    }

    @RequestMapping("/addIndex")
    public String addIndex(Model model, String path, Index index) {
        switch (path){
            case "housearea":
                indexService.addHousearea(index);
                break;
            case "houseform":
                indexService.addHouseform(index);
                break;
            case "housetype":
                indexService.addHousetype(index);
                break;
            case "styleindex":
                indexService.addStyleindex(index);
                break;
            case "workertype":
                indexService.addworkertype(index);
                break;
        }
        return "redirect:/admin/indexList/"+path;
    }

    @RequestMapping("/modifyIndex")
    public String modifyIndex(Model model, String path, Index index) {
        switch (path){
            case "housearea":
                indexService.modifyHousearea(index);
                break;
            case "houseform":
                indexService.modifyHouseform(index);
                break;
            case "housetype":
                indexService.modifyHousetype(index);
                break;
            case "styleindex":
                indexService.modifyStyleindex(index);
                break;
            case "workertype":
                indexService.modifyworkertype(index);
                break;
        }
        return "redirect:/admin/indexList/"+path;
    }

    @ResponseBody
    @RequestMapping("/delIndex")
    public void delIndex(Model model, String path, int id) {
        switch (path){
            case "housearea":
                indexService.delHousearea(id);
                break;
            case "houseform":
                indexService.delHouseform(id);
                break;
            case "housetype":
                indexService.delHousetype(id);
                break;
            case "styleindex":
                indexService.delStyleindex(id);
                break;
            case "workertype":
                indexService.delworkertype(id);
                break;
        }
    }

    @RequestMapping("/bookList")
    public String bookList(Model model) {
        List<Book> books = bookService.selectBook();
        model.addAttribute("books", books);
        return "Admin/book-list";
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
        return "Admin/book";
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
        return "redirect:/admin/book/" + book.getId();
    }

}
