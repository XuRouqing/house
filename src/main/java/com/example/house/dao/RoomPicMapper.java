package com.example.house.dao;

import com.example.house.pojo.RoomPic;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface RoomPicMapper {
    void insertRoomPic(RoomPic roomPicPic);//addRoomPic
    public RoomPic selectRoomPicById(int picId);//findRoomPicById
    public List<RoomPic> selectRoomPicAll();//getRoomPicList
    public List<RoomPic> selectRoomPicByRoomId(int roomid);//getRoomPicByRoomId
    public void modifyRoomPic(RoomPic roomPic);//modifyRoomPic
    public void deleteRoomPic(Integer picId);//deleteRoomPic
}
