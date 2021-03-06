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
@ConfigurationProperties(prefix = "setorder")
public class SetOrder {
    private int id;
    private int setId;
    private int customerId;
    private String customerName;
    private String tel;
    private String email;
    private String province;
    private String city;
    private String position;
    private String time;
    private int status;
    private String location;
    private String setName;
}
