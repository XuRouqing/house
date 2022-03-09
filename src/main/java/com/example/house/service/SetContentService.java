package com.example.house.service;

import com.example.house.pojo.SetContent;

import java.util.List;

public interface SetContentService {
    public void addSetContent(SetContent setContent);
    public SetContent findSetContentById(Integer setContentId);
    public List<SetContent> getSetContentList();
    public List<SetContent> getSetContentListBySet(int setId);
    public List<SetContent> getSetContentListBySetId(int setId);
    public void modifySetContent(SetContent setContent);
    public void deleteSetContent(Integer setContentId);
}
