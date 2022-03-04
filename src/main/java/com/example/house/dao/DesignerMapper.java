package com.example.house.dao;

import com.example.house.pojo.Designer;
import com.example.house.pojo.Index;
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
    public List<Designer> findDesignerByName(@Param("name") String name);
    public List<Designer> selectDesignerBySL(String style, String level);
    public List<Designer> selectTopNDesigner(int n);
    public void modifyDesignerAll(Designer designer);
    public void modifyDesignerMain(Designer designer);
    public void modifyDesigner(Designer designer);
    public void deleteDesigner(Integer id);
    public List<Index> selectDesignerLevel();
    public List<Index> selectDesignerStyle();
    public int selectDesignerNum();
    public int selectHouseNumByDesignerId(int id);
    public int selectOrderNumByDesignerId(int id);
    public Designer selectDesignerByUserId(int id);
}
