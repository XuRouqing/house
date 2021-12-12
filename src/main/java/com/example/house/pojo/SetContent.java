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
@ConfigurationProperties(prefix = "setcontent")
public class SetContent {
    private int contentId;
    private int setId;
    private String name;
    private String pic;
    private int rownum;
}
