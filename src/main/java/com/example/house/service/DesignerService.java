package com.example.house.service;

import com.example.house.pojo.Designer;

import java.util.List;

public interface DesignerService {
    public void addDesigner(Designer designer);
    public Designer findDesignerById(Integer id);
    public List<Designer> getDesignerList();
    public List<Designer> getDesignerListByPage(int pageNow, int pageCount);
    public Designer findDesignerByName(String name);
    public void modifyDesigner(Designer designer);
    public void deleteDesigner(Integer id);
}
