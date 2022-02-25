package com.example.house.service;

import com.example.house.pojo.Appointment;

import java.util.List;

public interface AppointmentService {
    public void addAppointment(Appointment appointment);
    public int getAppointmentNum();
    public List<Appointment> getAppointmentList();
    public List<Appointment> getAppointmentListByDesigner(int designerId);
    public List<Appointment> getAppointmentListByCustomer(int customerId);
}
