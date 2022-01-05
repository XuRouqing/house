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
    private String tel;
    private String email;
    private String city;
    private String time;
}
