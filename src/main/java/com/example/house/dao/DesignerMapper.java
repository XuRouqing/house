package com.example.house.dao;

import com.example.house.pojo.Designer;
import com.example.house.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import sun.security.krb5.internal.crypto.Des;

import java.util.List;

@Component
@Mapper
public interface DesignerMapper {
    void insertDesigner(Designer designer);
    Designer selectDesignerById(int id);
    List<Designer> selectDesignerAll();
    public Designer findDesignerByName(@Param("name") String name);
    public List<Designer> selectDesignerBySL(String style, String level);
    public List<Designer> selectTopNDesigner(int n);
    public void modifyDesigner(Designer designer);
    public void deleteDesigner(Integer id);
}