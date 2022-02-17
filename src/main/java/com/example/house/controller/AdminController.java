package com.example.house.controller;

import com.example.house.pojo.*;
import com.example.house.service.*;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sun.security.krb5.internal.crypto.Des;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
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

    @RequestMapping("/index")
    public String toindex(Model model) {
        int workerNum = workerService.getWorkerNum();
        int designerNum = designerService.getDesignerNum();
        List<Designer> designers = designerService.getDesignerList();
        List<Worker> workers = workerService.getWorkerList();
        model.addAttribute("workerNum",workerNum);
        model.addAttribute("designerNum",designerNum);
        model.addAttribute("workers",workers);
        model.addAttribute("designers",designers);
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

    @RequestMapping("/modifyDesigner1")
    public String modifyDesigner1(Model model, Designer designer){
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
        model.addAttribute("workerInfo",worker);
        model.addAttribute("workerType",workerType);
        return "Admin/worker";
    }

    @ResponseBody
    @RequestMapping("/delWorker")
    public String delWorker(Model model, int id){
        try {
            workerService.deleteWorker(id);
            return "success";
        }catch (Exception e){
            return e.getMessage();
        }
    }
}
