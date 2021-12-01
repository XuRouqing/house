package com.example.house.service;

import com.example.house.pojo.Room;

import java.util.List;

public interface RoomService {
    public void addRoom(Room room);
    public Room findRoomById(Integer roomId);
    public List<Room> getRoomList();
    public List<Room> getRoomByHouseId(Integer houseId);
    public List<Room> getRoomByType(String roomType);
    public List<Room> getRoomByspecificType(String specificType);
    public List<Room> getRoomByStyle(String style);
    public void modifyRoom(Room room);
    public void deleteRoom(Integer roomId);
}
