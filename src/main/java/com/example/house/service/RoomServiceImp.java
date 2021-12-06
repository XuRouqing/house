package com.example.house.service;

import com.example.house.dao.HouseMapper;
import com.example.house.dao.RoomMapper;
import com.example.house.pojo.House;
import com.example.house.pojo.Room;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoomServiceImp implements RoomService  {
    @Autowired
    private RoomMapper roomMapper;

    @Override
    public void addRoom(Room room){
        this.roomMapper.insertRoom(room);
    }

    @Override
    public Room findRoomById(Integer roomId){
        return roomMapper.selectRoomById(roomId);
    }

    @Override
    public List<Room> getRoomList(){
        return roomMapper.selectRoomAll();
    }

    @Override
    public List<Room> getRoomByHouseId(Integer houseId){
        return roomMapper.getRoomByHouseId(houseId);
    }

    @Override
    public List<Room> getRoomByType(String type){
        return roomMapper.selectRoomByType(type);
    }

    @Override
    public List<Room> getRoomByStyle(String style){
        return roomMapper.selectRoomByStyle(style);
    }

    @Override
    public List<Room> getRoomByspecificType(String specificType){
        return roomMapper.selectRoomByspecificType(specificType);
    }

    @Override
    public void modifyRoom(Room room){
        roomMapper.modifyRoom(room);
    }

    @Override
    public void deleteRoom(Integer roomId){
        roomMapper.deleteRoom(roomId);
    }
}
