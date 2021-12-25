package com.example.house.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "appointment")
public class Appointment {
    private int appointmentId;
    private int designerId;
    private int customerId;
    private String date;
    private String time;
    private String customerTel;
    private String customerName;
    private String customerEmail;
    private String message;
    private String location;

}
