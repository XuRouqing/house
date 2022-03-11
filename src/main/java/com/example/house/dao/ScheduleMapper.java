package com.example.house.dao;

import com.example.house.pojo.Schedule;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface ScheduleMapper {
    public List<Schedule> getScheduleByDesignerId(int id);
    public void insertSchedule(Schedule schedule);
    public void deleteSchedule(int id);
}
