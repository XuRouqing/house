package com.example.house.service;

import com.example.house.dao.SetConfigMapper;
import com.example.house.pojo.SetConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SetConfigServiceImp implements SetConfigService  {
    @Autowired
    private SetConfigMapper setConfigMapper;

    @Override
    public void addSetConfig(SetConfig setConfig){
        this.setConfigMapper.insertSetConfig(setConfig);
    }

    @Override
    public SetConfig findSetConfigById(Integer setConfigId){
        return setConfigMapper.selectSetConfigById(setConfigId);
    }

    @Override
    public List<SetConfig> getSetConfigList(){
        return setConfigMapper.selectSetConfigAll();
    }

    @Override
    public List<SetConfig> getSetConfigListBySet(int setId){
        return setConfigMapper.selectSetConfigBySet(setId);
    }

    @Override
    public void modifySetConfig(SetConfig setConfig){
        setConfigMapper.modifySetConfig(setConfig);
    }

    @Override
    public void deleteSetConfig(Integer setConfigId){
        setConfigMapper.deleteSetConfig(setConfigId);
    }
}
