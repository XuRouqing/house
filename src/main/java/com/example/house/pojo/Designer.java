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
@ConfigurationProperties(prefix = "designer")
public class Designer {
    private int id;
    private String name;
    private String gender;
    private int workingAge;
    private String level;//等级
    private String style;//擅长风格
    private String represent;//代表作
    private String tel;
    private int age;
    private String pic;
}
