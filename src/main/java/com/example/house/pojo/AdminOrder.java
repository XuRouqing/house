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
    private int id;
    private String name;
    private String tel;
    private String email;
    private String loc;
    private String message;
    private int status;
    private int type;
}
