package com.example.house.dao;

import com.example.house.pojo.City;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface CityMapper {
    public List<City> selectCityAll();//getCityList
    public List<City> selectProvince();//
    public List<City> selectCityByPid(int id);//
}
