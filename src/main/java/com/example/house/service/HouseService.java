package com.example.house.service;

import com.example.house.pojo.Designer;
import com.example.house.pojo.House;

import java.util.List;

public interface HouseService {
    public void addHouse(House house);
    public House findHouseById(Integer houseId);
    public List<House> getHouseList();
    public List<House> getHouseListByPage(int pageNow, int pageCount);
    public List<House> getHouseByPageAndSTA(int pageNow, int pageCount, String style, String type, String area);
    public List<House> findHouseByDesignerId(int designerId);
    public void modifyHouse(House house);
    public void deleteHouse(Integer houseId);
}
