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
@ConfigurationProperties(prefix = "house")
public class House {
    private int houseId;
    private String style;//风格
    private String styleValue;
    private String houseType;//户型
    private double area;
    private String areaValue;
    private String formValue;
    private String city;
    private String local;//具体地点
    private String time;
    private String original;//原始户型图
    private String plane;//平面图
    private double price;
    private int designerId;
    private String workerIds;
    private String type;//类型（全包、半包、旧改）
    private String typeValue;
    private String roomIds;
    private String title;
    private String describe;
    private String mainPic;
    private String designer;
}
