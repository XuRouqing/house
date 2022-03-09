package com.example.house.dao;

import com.example.house.pojo.SetConfig;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface SetConfigMapper {
    void insertSetConfig(SetConfig setConfig);//addSet
    public SetConfig selectSetConfigById(int setConfigId);//findSetConfigById
    public List<SetConfig> selectSetConfigAll();//getSetConfigList
    public List<SetConfig> selectSetConfigBySet(int setId);//getSetConfigListBySet
    public List<SetConfig> selectSetConfigByContentId(int contentId);//
    public void modifySetConfig(SetConfig setConfig);//modifySetConfig
    public void deleteSetConfig(Integer setConfigId);//deleteSetConfig
}
