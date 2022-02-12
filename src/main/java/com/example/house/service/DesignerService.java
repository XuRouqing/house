package com.example.house.service;

import com.example.house.pojo.Designer;
import com.example.house.pojo.Index;

import java.util.List;

public interface DesignerService {
    public void addDesigner(Designer designer);
    public Designer findDesignerById(Integer id);
    public List<Designer> getDesignerList();
    public List<Designer> getDesignerListByPage(int pageNow, int pageCount);
    public List<Designer> getDesignerByPageAndSL(int pageNow, int pageCount, String style, String level);
    public List<Designer> getTopNDesigner(int n);
    public List<Designer> findDesignerByName(String name);
    public void modifyDesignerALL(Designer designer);
    public void modifyDesignerMain(Designer designer);
    public void modifyDesigner(Designer designer);
    public void deleteDesigner(Integer id);
    public List<Index> getDesignerLevel();
    public List<Index> getDesignerStyle();
    public int getDesignerNum();
}
