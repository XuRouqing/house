package com.example.house.service;

import com.example.house.dao.CityMapper;
import com.example.house.dao.SetMapper;
import com.example.house.pojo.City;
import com.example.house.pojo.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CityServiceImp implements CityService  {
    @Autowired
    private CityMapper cityMapper;

    @Override
    public List<City> getCityList(){
        return cityMapper.selectCityAll();
    }

    @Override
    public List<City> getProvinceList(){
        return cityMapper.selectProvince();
    }

    @Override
    public List<City> getCityListByPid(int id){
        return cityMapper.selectCityByPid(id);
    }

    @Override
    public City getCityListById(int id){
        return cityMapper.selectCityById(id);
    }

    @Override
    public String getCityNameById(int id){
        return cityMapper.selectCityNameById(id);
    }

}
