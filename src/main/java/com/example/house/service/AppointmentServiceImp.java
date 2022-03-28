package com.example.house.service;


import com.example.house.dao.AppointmentMapper;
import com.example.house.pojo.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AppointmentServiceImp implements AppointmentService  {
    @Autowired
    private AppointmentMapper appointmentMapper;

    @Override
    public void addAppointment(Appointment appointment) {
        this.appointmentMapper.insertAppointment(appointment);
    }

    @Override
    public int getAppointmentNum() {
        return this.appointmentMapper.selectAppointmentNum();
    }

    @Override
    public List<Appointment> getAppointmentList() {
        return this.appointmentMapper.selectAppointmentAll();
    }

    @Override
    public Appointment getAppointmentById(int id) {
        return this.appointmentMapper.selectAppointmentById(id);
    }

    @Override
    public List<Appointment> getAppointmentListByDesigner(int designerId) {
        return this.appointmentMapper.selectAppointmentByDesigner(designerId);
    }

    @Override
    public List<Appointment> getAppointmentListByCustomer(int customerId) {
        return this.appointmentMapper.selectAppointmentByCustomer(customerId);
    }

    @Override
    public void updateAppointmentStatus(int id, int status) {
        this.appointmentMapper.updateAppointmentStatus(id, status);
    }

    @Override
    public void updateAppointmentStatusEveryday() {
        this.appointmentMapper.updateAppointmentStatusEveryday();
    }

    @Override
    public void deleteAppointment(Integer id) {
        this.appointmentMapper.deleteAppointment(id);
    }

    @Override
    public List<Appointment> getAppointmentByDesignerId(int id) {
        return this.appointmentMapper.selectAppointmentByDesignerId(id);
    }

    @Override
    public List<Appointment> getAppointmentAllByDesignerId(int id) {
        return this.appointmentMapper.selectAppointmentAllByDesignerId(id);
    }
}
