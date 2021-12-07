package com.example.house.controller;

import com.example.house.pojo.House;
import com.example.house.pojo.Room;
import com.example.house.service.HouseService;
import com.example.house.service.RoomService;
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

    @RequestMapping("/{id}")
    public String toCase(@PathVariable int id, Model model) {
        House house = houseService.findHouseById(id);
        List<Room> rooms = roomService.getRoomByHouseId(id);
        model.addAttribute("houseInfo",house);
        model.addAttribute("roomsInfo",rooms);
        return "case-detail";
    }
}
