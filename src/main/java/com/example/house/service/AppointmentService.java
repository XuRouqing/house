package com.example.house.service;

import com.example.house.pojo.Appointment;

import java.util.List;

public interface AppointmentService {
    public void addAppointment(Appointment appointment);
    public int getAppointmentNum();
    public void deleteAppointment(Integer id);
    public List<Appointment> getAppointmentList();
    public List<Appointment> getAppointmentListByDesigner(int designerId);
    public List<Appointment> getAppointmentListByCustomer(int customerId);
    public void updateAppointmentStatus(int id, int status);
    public void updateAppointmentStatusEveryday();
}
