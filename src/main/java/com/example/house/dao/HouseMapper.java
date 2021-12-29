package com.example.house.dao;

import com.example.house.pojo.Designer;
import com.example.house.pojo.House;
import com.example.house.pojo.Index;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface HouseMapper {
    void insertHouse(House house);
    House selectHouseById(int houseId);
    List<House> selectHouseAll();
    public List<House> findHouseByDesignerId(@Param("designerId") int designerId);
    public List<House> selectHouseByPageAndSTFA(String style, String type, String form, String area);
    public void modifyHouse(House house);
    public void deleteHouse(Integer houseId);
    public List<Index> selectHouseStyle();
    public List<Index> selectHouseType();
    public List<Index> selectHouseForm();
    public List<Index> selectHouseArea();
}
