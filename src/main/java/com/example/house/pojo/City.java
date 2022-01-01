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
@ConfigurationProperties(prefix = "city")
public class City {
    private int id;
    private int pid;//父类id
    private String district_name;//城市的名字
    private int type;//城市的类型，0是国，1是省，2是市，3是区
    private int hierarchy;//地区所处的层级
    private String district_sqe;//层级序列
}
