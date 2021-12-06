package com.example.house.dao;

import com.example.house.pojo.Room;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface RoomMapper {
    void insertRoom(Room room);//addRoom
    public Room selectRoomById(int roomId);//findRoomById
    public List<Room> selectRoomAll();//getRoomList
    public List<Room> getRoomByHouseId(@Param("houseId") int houseId);//getRoomByHouseId
    public List<Room> selectRoomByType(String type);//getRoomByType
    public List<Room> selectRoomByStyle(String style);//getRoomByStyle
    public List<Room> selectRoomByspecificType(String specificType);//getRoomByspecificType
    public void modifyRoom(Room room);//modifyRoom
    public void deleteRoom(Integer roomId);//deleteRoom
}
