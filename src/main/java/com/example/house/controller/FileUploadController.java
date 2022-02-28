package com.example.house.controller;

import com.example.house.pojo.Designer;
import com.example.house.pojo.House;
import com.example.house.pojo.Room;
import com.example.house.pojo.Worker;
import com.example.house.service.DesignerService;
import com.example.house.service.HouseService;
import com.example.house.service.RoomService;
import com.example.house.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Controller
public class FileUploadController {
    @Value("${user.file.path}")
    private String filePath;

    @RequestMapping("/upload")
    public ModelAndView update(@RequestParam("pic") MultipartFile multipartFile){
        SimpleDateFormat sdf = null;
        System.out.println(multipartFile);
        try{
            sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String timeStamp = sdf.format(new Date());
            String fileName = multipartFile.getOriginalFilename();//获取文件名称
            String suffixName=fileName.substring(fileName.lastIndexOf("."));
            File file = new File(filePath+timeStamp+suffixName);
            multipartFile.transferTo(file);
        }catch (IOException e){
            e.printStackTrace();
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }

}
