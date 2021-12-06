package com.example.house.service;

import com.example.house.pojo.RoomPic;

import java.util.List;

public interface RoomPicService {
    public void addRoomPic(RoomPic roomPic);
    public RoomPic findRoomPicById(Integer picId);
    public List<RoomPic> getRoomPicList();
    public List<RoomPic> getRoomPicByRoomId(int roomid);
    public void modifyRoomPic(RoomPic roomPic);
    public void deleteRoomPic(Integer picId);
}
