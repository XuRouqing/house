package com.example.house.dao;

import com.example.house.pojo.SetOrder;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface SetOrderMapper {
    void insertOrder(SetOrder setOrder);
    List<SetOrder> selectSetOrderAll();
    public SetOrder selectSetOrderById(int id);
    public List<SetOrder> selectSetOrderByCustomerId(int id);
    void updateSetOrderStatus(int id, int status);
    void delSetOrder(int id);
    int selectSetOrderNum();
}
