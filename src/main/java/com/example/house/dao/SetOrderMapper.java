package com.example.house.dao;

import com.example.house.pojo.SetOrder;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface SetOrderMapper {
    void insertOrder(SetOrder setOrder);
}
