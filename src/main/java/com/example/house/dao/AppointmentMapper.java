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
    int selectAppointmentNum();
    public void deleteAppointment(Integer id);
    List<Appointment> selectAppointmentAll();
    Appointment selectAppointmentById(int id);
    List<Appointment> selectAppointmentByDesigner(int designerId);
    List<Appointment> selectAppointmentByCustomer(int customerId);
    void updateAppointmentStatus(int id, int status);
    void updateAppointmentStatusEveryday();
    public List<Appointment> selectAppointmentByDesignerId(int id);
    public List<Appointment> selectAppointmentAllByDesignerId(int id);
}
