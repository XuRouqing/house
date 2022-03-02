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
        registry.addResourceHandler("/userPic/**").addResourceLocations("file:"+path);
        registry.addResourceHandler("/workerPic/**").addResourceLocations("file:"+workerPath);
    }
}

