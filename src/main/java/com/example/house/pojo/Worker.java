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
@ConfigurationProperties(prefix = "worker")
public class Worker {
    private int id;
    private String name;
    private String gender;
    private String city;
    private int cityId;
    private String type;//工种
    private String typeValue;//工种
    private int age;
    private int workingAge;
    private String represent;
    private String tel;
    private String pic;
    private String describe;
    private String declaration;
}
