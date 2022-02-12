package com.example.house.controller;

import com.example.house.pojo.*;
import com.example.house.service.*;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sun.security.krb5.internal.crypto.Des;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

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

    @RequestMapping("/worker/{id}")
    public String toworker(@PathVariable int id, Model model) {
        Worker worker = workerService.findWorkerById(id);
        model.addAttribute("workerInfo",worker);
        return "Admin/worker";
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
        model.addAttribute("designer",designer);
        return "Admin/designer";
    }

    @RequestMapping("/modifyDesigner1")
    public String tobookOnline(Model model, Designer designer){
        designerService.modifyDesignerMain(designer);
        return  "redirect:/admin/designer/"+designer.getId();
    }

    @RequestMapping("/workerList")
    public String toworkerList(Model model) {
        List<Worker> workers = workerService.getWorkerList();
        model.addAttribute("workers",workers);
        return "Admin/worker-list";
    }
}
