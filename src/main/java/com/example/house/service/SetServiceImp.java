package com.example.house.service;

import com.example.house.dao.SetMapper;
import com.example.house.pojo.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SetServiceImp implements SetService  {
    @Autowired
    private SetMapper setMapper;

    @Override
    public void addSet(Set set){
        this.setMapper.insertSet(set);
    }

    @Override
    public Set findSetById(Integer setId){
        return setMapper.selectSetById(setId);
    }

    @Override
    public List<Set> getSetList(){
        return setMapper.selectSetAll();
    }

    @Override
    public void modifySet(Set set){
        setMapper.modifySet(set);
    }

    @Override
    public void deleteSet(Integer setId){
        setMapper.deleteSet(setId);
    }
}
