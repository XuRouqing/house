package com.example.house.controller;

import com.example.house.pojo.Designer;
import com.example.house.pojo.House;
import com.example.house.pojo.Room;
import com.example.house.pojo.Worker;
import com.example.house.service.DesignerService;
import com.example.house.service.HouseService;
import com.example.house.service.RoomService;
import com.example.house.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
        return "Admin/index";
    }

    @RequestMapping("/worker/{id}")
    public String toworker(@PathVariable int id, Model model) {
        Worker worker = workerService.findWorkerById(id);
        model.addAttribute("workerInfo",worker);
        return "Admin/worker";
    }
}
