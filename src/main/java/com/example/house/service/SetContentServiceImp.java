package com.example.house.service;


import com.example.house.dao.SetContentMapper;
import com.example.house.pojo.SetContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SetContentServiceImp implements SetContentService  {
    @Autowired
    private SetContentMapper setContentMapper;

    @Override
    public void addSetContent(SetContent setContent){
        this.setContentMapper.insertSetContent(setContent);
    }

    @Override
    public SetContent findSetContentById(Integer setContentId){
        return setContentMapper.selectSetContentById(setContentId);
    }

    @Override
    public List<SetContent> getSetContentList(){
        return setContentMapper.selectSetContentAll();
    }

    @Override
    public List<SetContent> getSetContentListBySet(int setId){
        return setContentMapper.selectSetContentBySet(setId);
    }

    @Override
    public List<SetContent> getSetContentListBySetId(int setId){
        return setContentMapper.selectSetContentBySetId(setId);
    }

    @Override
    public void modifySetContent(SetContent setContent){
        setContentMapper.modifySetContent(setContent);
    }

    @Override
    public void deleteSetContent(Integer setContentId){
        setContentMapper.deleteSetContent(setContentId);
    }
}
