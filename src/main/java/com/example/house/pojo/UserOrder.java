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
@ConfigurationProperties(prefix = "userorder")
public class UserOrder {
    private String time;
    private String type;
    private String status;
    private int id;
    private int typeValue;//营销活动1 设计师预约2 在线预约3
}
