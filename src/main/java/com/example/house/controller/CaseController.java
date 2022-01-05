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
@RequestMapping("/case")
public class CaseController {

    @Autowired
    private HouseService houseService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private DesignerService designerService;

    @Autowired
    private WorkerService workerService;

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
}
