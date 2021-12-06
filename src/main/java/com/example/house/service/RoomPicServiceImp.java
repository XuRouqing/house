package com.example.house.service;

import com.example.house.dao.RoomPicMapper;
import com.example.house.pojo.RoomPic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoomPicServiceImp implements RoomPicService  {
    @Autowired
    private RoomPicMapper roomPicMapper;

    @Override
    public void addRoomPic(RoomPic roomPic){
        this.roomPicMapper.insertRoomPic(roomPic);
    }

    @Override
    public RoomPic findRoomPicById(Integer picId){
        return roomPicMapper.selectRoomPicById(picId);
    }

    @Override
    public List<RoomPic> getRoomPicList(){
        return roomPicMapper.selectRoomPicAll();
    }

    @Override
    public List<RoomPic> getRoomPicByRoomId(int roomid){
        return roomPicMapper.selectRoomPicByRoomId(roomid);
    }

    @Override
    public void modifyRoomPic(RoomPic roomPic){
        roomPicMapper.modifyRoomPic(roomPic);
    }

    @Override
    public void deleteRoomPic(Integer picId){
        roomPicMapper.deleteRoomPic(picId);
    }
}
