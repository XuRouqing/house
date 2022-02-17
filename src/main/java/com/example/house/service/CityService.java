package com.example.house.service;

import com.example.house.pojo.City;
import com.example.house.pojo.Set;

import java.util.List;

public interface CityService {
    public List<City> getCityList();
    public List<City> getProvinceList();
    public List<City> getCityListByPid(int id);
    public City getCityListById(int id);
}
