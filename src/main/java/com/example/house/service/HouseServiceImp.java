package com.example.house.service;

import com.example.house.dao.HouseMapper;
import com.example.house.pojo.House;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class HouseServiceImp implements HouseService  {
    @Autowired
    private HouseMapper houseMapper;

    @Override
    public void addHouse(House house){
        this.houseMapper.insertHouse(house);
    }

    @Override
    public House findHouseById(Integer houseId){
        return houseMapper.selectHouseById(houseId);
    }

    @Override
    public List<House> getHouseList(){
        return houseMapper.selectHouseAll();
    }

    @Override
    public List<House> getHouseListByPage(int pageNow, int pageCount){
        PageHelper.startPage(pageNow, pageCount);
        List<House> houses = houseMapper.selectHouseAll();
        return houses;
    }

    @Override
    public List<House> findHouseByDesignerId(int designerId){
        return houseMapper.findHouseByDesignerId(designerId);
    }

    @Override
    public void modifyHouse(House house){
        houseMapper.modifyHouse(house);
    }

    @Override
    public void deleteHouse(Integer houseId){
        houseMapper.deleteHouse(houseId);
    }
}
