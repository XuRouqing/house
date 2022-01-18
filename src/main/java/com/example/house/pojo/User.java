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
@ConfigurationProperties(prefix = "user")
public class User {
    private int id;
    private String code;
    private String name;
    private String role;
    private String password;
    private String email;
    private String phone;
}
