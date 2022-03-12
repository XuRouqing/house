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
@ConfigurationProperties(prefix = "book")
public class Book {
    private int id;
    private Integer customerId;
    private String name;
    private String tel;
    private String email;
    private int province;
    private int city;
    private String location;
    private int designerId;
    private String workers;
    private String time;
    private String remarks;
    private String cityStr;
    private int status;
    private String designerName;
}
