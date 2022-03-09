package com.example.house.dao;

import com.example.house.pojo.SetContent;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface SetContentMapper {
    void insertSetContent(SetContent setContent);//addSet
    public SetContent selectSetContentById(int setContentId);//findSetContentById
    public List<SetContent> selectSetContentAll();//getSetContentList
    public List<SetContent> selectSetContentBySet(int setId);//getSetContentListBySet
    public List<SetContent> selectSetContentBySetId(int setId);//getSetContentListBySet
    public void modifySetContent(SetContent setContent);//modifySetContent
    public void deleteSetContent(Integer setContentId);//deleteSetContent
}
