package com.example.house.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "adminorder")
public class AdminOrder {
    private int appointmentId;
    private String customerName;
    private String customerTel;
    private String customerEmail;
    private String location;
    private String message;
    private int status;
    private int type;
    private String day;
    private String time;
}
