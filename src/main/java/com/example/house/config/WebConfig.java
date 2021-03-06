package com.example.house.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //关于图片上传后需要重启服务器才能刷新图片
        //这是一种保护机制，为了防止绝对路径被看出来，目录结构暴露
        //解决方法:将虚拟路径/userPic/
        //向绝对路径 (D:/College/Graduation project/house/src/main/resources/static/userPic/)映射
        String path="D:/College/Graduation project/house/src/main/resources/static/userPic/";
        String workerPath="D:/College/Graduation project/house/src/main/resources/static/workerPic/";
        String setImagePath="D:/College/Graduation project/house/src/main/resources/static/setImage/";
        String mainPicPath="D:/College/Graduation project/house/src/main/resources/static/mainPic/";
        String originalPath="D:/College/Graduation project/house/src/main/resources/static/original/";
        String planePath="D:/College/Graduation project/house/src/main/resources/static/plane/";
        String roomPath="D:/College/Graduation project/house/src/main/resources/static/room/";
        registry.addResourceHandler("/userPic/**").addResourceLocations("file:"+path);
        registry.addResourceHandler("/workerPic/**").addResourceLocations("file:"+workerPath);
        registry.addResourceHandler("/setImage/**").addResourceLocations("file:"+setImagePath);
        registry.addResourceHandler("/mainPic/**").addResourceLocations("file:"+mainPicPath);
        registry.addResourceHandler("/original/**").addResourceLocations("file:"+originalPath);
        registry.addResourceHandler("/plane/**").addResourceLocations("file:"+planePath);
        registry.addResourceHandler("/room/**").addResourceLocations("file:"+roomPath);
    }
}

