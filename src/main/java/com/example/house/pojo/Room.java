package com.example.house.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "room")
public class Room {
    private int roomId;
    private int houseId;//所属房子
    private String roomType;//类别
    private String specificType;//具体类别（主卧、次卧等）
    private String style;//风格
    private double price;
    private List<RoomPic> roomPics;
//    private String href;
}
