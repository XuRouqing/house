package com.example.house.service;

import com.example.house.dao.DesignerMapper;
import com.example.house.pojo.Designer;
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
    public Designer findDesignerByName(String name){
        return designerMapper.findDesignerByName(name);
    }

    @Override
    public void modifyDesigner(Designer designer){
        designerMapper.modifyDesigner(designer);
    }

    @Override
    public void deleteDesigner(Integer id){
        designerMapper.deleteDesigner(id);
    }
}
