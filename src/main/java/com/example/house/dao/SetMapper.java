package com.example.house.dao;

import com.example.house.pojo.Set;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface SetMapper {
    void insertSet(Set set);//addSet
    public Set selectSetById(int setId);//findSetById
    public List<Set> selectSetAll();//getSetList
    public void modifySet(Set set);//modifySet
    public void deleteSet(Integer setId);//deleteSet
}
