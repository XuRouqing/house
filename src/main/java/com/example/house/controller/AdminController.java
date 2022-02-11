package com.example.house.controller;

import com.example.house.pojo.Designer;
import com.example.house.pojo.House;
import com.example.house.pojo.Room;
import com.example.house.pojo.Worker;
import com.example.house.service.DesignerService;
import com.example.house.service.HouseService;
import com.example.house.service.RoomService;
import com.example.house.service.WorkerService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sun.security.krb5.internal.crypto.Des;

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
    @RequestMapping("/designerList")
    public List<Designer> getDesignerList(@RequestParam("pageNow") int pageNow,
                                          @RequestParam("pageCount") int pageCount){
        List<Designer> designerList = designerService.getDesignerListByPage(pageNow,pageCount);
        return designerList;
    }

    @ResponseBody
    @RequestMapping("/workerList")
    public List<Worker> getWorkerList(@RequestParam("pageNow") int pageNow,
                                          @RequestParam("pageCount") int pageCount){
        List<Worker> workerList = workerService.getWorkerListByPage(pageNow,pageCount);
        return workerList;
    }
}
