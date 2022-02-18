package com.example.house.service;

import com.example.house.dao.DesignerMapper;
import com.example.house.pojo.Designer;
import com.example.house.pojo.Index;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DesignerServiceImp implements DesignerService  {
    @Autowired
    private DesignerMapper designerMapper;

    @Override
    public void addDesigner(Designer designer){
        this.designerMapper.insertDesigner(designer);
    }

    @Override
    public Designer findDesignerById(Integer id){
        return designerMapper.selectDesignerById(id);
    }

    @Override
    public List<Designer> getDesignerList(){
        return designerMapper.selectDesignerAll();
    }

    @Override
    public List<Designer> getDesignerListByPage(int pageNow, int pageCount){
        PageHelper.startPage(pageNow, pageCount);
        List<Designer> designers = designerMapper.selectDesignerAll();
        return designers;
    }

    @Override
    public List<Designer> getDesignerByPageAndSL(int pageNow, int pageCount, String style, String level){
        PageHelper.startPage(pageNow, pageCount);
        List<Designer> designers = designerMapper.selectDesignerBySL(style, level);
        return designers;
    }

    @Override
    public List<Designer> getTopNDesigner(int n){
        List<Designer> designers = designerMapper.selectTopNDesigner(n);
        return designers;
    }

    @Override
    public List<Designer> findDesignerByName(String name){
        return designerMapper.findDesignerByName(name);
    }

    @Override
    public void modifyDesignerALL(Designer designer){
        designerMapper.modifyDesignerAll(designer);
    }

    @Override
    public void modifyDesignerMain(Designer designer){
        designerMapper.modifyDesignerMain(designer);
    }

    @Override
    public void modifyDesigner(Designer designer){
        designerMapper.modifyDesigner(designer);
    }

    @Override
    public void deleteDesigner(Integer id){
        designerMapper.deleteDesigner(id);
    }

    @Override
    public List<Index> getDesignerLevel(){
        return designerMapper.selectDesignerLevel();
    }

    @Override
    public List<Index> getDesignerStyle(){
        return designerMapper.selectDesignerStyle();
    }

    @Override
    public int getDesignerNum(){
        return designerMapper.selectDesignerNum();
    }
}
