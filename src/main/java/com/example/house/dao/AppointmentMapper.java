package com.example.house.dao;

import com.example.house.pojo.Appointment;
import com.example.house.pojo.Designer;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface AppointmentMapper {
    void insertAppointment(Appointment appointment);
    List<Appointment> selectAppointmentAll();
    List<Appointment> selectAppointmentByDesigner(int designerId);
    List<Appointment> selectAppointmentByCustomer(int customerId);
}
