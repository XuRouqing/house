package com.example.house.service;

import com.example.house.pojo.Schedule;

import java.util.List;

public interface ScheduleService {
    public List<Schedule> getScheduleByDesignerId(int id);
}
