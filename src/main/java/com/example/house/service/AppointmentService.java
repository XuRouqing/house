package com.example.house.service;

import com.example.house.pojo.Appointment;
import com.example.house.pojo.Designer;

import java.util.List;

public interface AppointmentService {
    public void addAppointment(Appointment appointment);
    public int getAppointmentNum();
    public void deleteAppointment(Integer id);
    public List<Appointment> getAppointmentList();
    public Appointment getAppointmentById(int id);
    public List<Appointment> getAppointmentListByDesigner(int designerId);
    public List<Appointment> getAppointmentListByCustomer(int customerId);
    public void updateAppointmentStatus(int id, int status);
    public void updateAppointmentStatusEveryday();
    public List<Appointment> getAppointmentByDesignerId(int id);
    public List<Appointment> getAppointmentAllByDesignerId(int id);
}
