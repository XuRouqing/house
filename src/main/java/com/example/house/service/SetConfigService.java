package com.example.house.service;

import com.example.house.pojo.SetConfig;

import java.util.List;

public interface SetConfigService {
    public void addSetConfig(SetConfig setConfig);
    public SetConfig findSetConfigById(Integer setConfigId);
    public List<SetConfig> getSetConfigList();
    public List<SetConfig> getSetConfigListBySet(int setId);
    public List<SetConfig> getSetConfigListByContentId(int contentId);
    public void modifySetConfig(SetConfig setConfig);
    public void deleteSetConfig(Integer setConfigId);
}
