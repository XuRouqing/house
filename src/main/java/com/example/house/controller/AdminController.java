package com.example.house.controller;

import com.example.house.pojo.*;
import com.example.house.service.*;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @RequestMapping("/index")
    public String toindex(Model model) {
        int workerNum = workerService.getWorkerNum();
        int designerNum = designerService.getDesignerNum();
        List<Designer> designers = designerService.getDesignerList();
        List<Worker> workers = workerService.getWorkerList();
        int appointmentNum = appointmentService.getAppointmentNum();
        int setOrderNum = setOrderService.getSetOrderNum();
        int orderNum = appointmentNum+setOrderNum;
        int userNum = userService.getUserNum();
        model.addAttribute("workerNum",workerNum);
        model.addAttribute("designerNum",designerNum);
        model.addAttribute("workers",workers);
        model.addAttribute("designers",designers);
        model.addAttribute("orderNum",orderNum);
        model.addAttribute("userNum",userNum);
        return "Admin/index";
    }

    @ResponseBody
    @RequestMapping("/getdesignerList")
    public List<Designer> getDesignerList(@RequestParam("pageNow") int pageNow,
                                          @RequestParam("pageCount") int pageCount){
        List<Designer> designerList = designerService.getDesignerListByPage(pageNow,pageCount);
        return designerList;
    }

    @ResponseBody
    @RequestMapping("/getworkerList")
    public List<Worker> getWorkerList(@RequestParam("pageNow") int pageNow,
                                          @RequestParam("pageCount") int pageCount){
        List<Worker> workerList = workerService.getWorkerListByPage(pageNow,pageCount);
        return workerList;
    }

    @RequestMapping("/designerList")
    public String todesignerList(Model model) {
        List<Designer> designers = designerService.getDesignerList();
        model.addAttribute("designers",designers);
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
        model.addAttribute("designer",designer);
        model.addAttribute("designerLevel",designerLevel);
        model.addAttribute("designerStyle",designerStyle);
        model.addAttribute("styleList",styleListInt);
        return "Admin/designer";
    }

    @Value("${user.file.path}")
    private String filePath;

    @RequestMapping("/modifyDesigner1")
    public String modifyDesigner1(Model model, Designer designer,
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
        designerService.modifyDesignerMain(designer);
        return  "redirect:/admin/designer/"+designer.getId();
    }

    @RequestMapping("/modifyDesigner2")
    public String modifyDesigner2(Model model, Designer designer){
        designerService.modifyDesigner(designer);
        return  "redirect:/admin/designer/"+designer.getId();
    }

    @ResponseBody
    @RequestMapping("/delDesigner")
    public String delDesigner(Model model, int id){
        try {
            designerService.deleteDesigner(id);
            return "success";
        }catch (Exception e){
            return e.getMessage();
        }
    }

    @RequestMapping("/workerList")
    public String toworkerList(Model model) {
        List<Worker> workers = workerService.getWorkerList();
        model.addAttribute("workers",workers);
        return "Admin/worker-list";
    }

    @RequestMapping("/worker/{id}")
    public String toworker(@PathVariable int id, Model model) {
        Worker worker = workerService.findWorkerById(id);
        List<Index> workerType = workerService.getWorkerType();
        List<City> provinces = cityService.getProvinceList();
        City cityNow = cityService.getCityListById(worker.getCityId());
        City provinceNow = cityService.getCityListById(cityNow.getPid());
        model.addAttribute("workerInfo",worker);
        model.addAttribute("workerType",workerType);
        model.addAttribute("provinces",provinces);
        model.addAttribute("cityNow",cityNow);
        model.addAttribute("provinceNow",provinceNow);
        return "Admin/worker";
    }

    @RequestMapping("/modifyWorker1")
    public String modifyWorker1(Model model, Worker worker,
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
            worker.setPic(pic);
        }
        workerService.modifyWorkerMain(worker);
        return  "redirect:/admin/worker/"+worker.getId();
    }

    @RequestMapping("/modifyWorker2")
    public String modifyWorker2(Model model, Worker worker){
        workerService.modifyWorker(worker);
        return  "redirect:/admin/worker/"+worker.getId();
    }

    @ResponseBody
    @RequestMapping("/delWorker")
    public String delWorker(Model model, int id){
        System.out.println(id);
        try {
            workerService.deleteWorker(id);
            return "success";
        }catch (Exception e){
            return e.getMessage();
        }
    }

    @RequestMapping("/appointmentList")
    public String toappointmentList(Model model) {
        List<Appointment> appointments = appointmentService.getAppointmentList();
        model.addAttribute("appointments",appointments);
        appointmentService.updateAppointmentStatusEveryday();
        return "Admin/appointment-list";
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
}
