package com.example.house.service;

import com.example.house.dao.CityMapper;
import com.example.house.dao.ScheduleMapper;
import com.example.house.pojo.City;
import com.example.house.pojo.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ScheduleServiceImp implements ScheduleService  {
    @Autowired
    private ScheduleMapper scheduleMapper;

    @Override
    public List<Schedule> getScheduleByDesignerId(int id){
        return scheduleMapper.getScheduleByDesignerId(id);
    }

}
