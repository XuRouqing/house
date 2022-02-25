package com.example.house.service;

import com.example.house.dao.SetMapper;
import com.example.house.dao.SetOrderMapper;
import com.example.house.pojo.Set;
import com.example.house.pojo.SetOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SetOrderServiceImp implements SetOrderService  {
    @Autowired
    private SetOrderMapper setOrderMapper;

    @Override
    public void addOrder(SetOrder order){
        this.setOrderMapper.insertOrder(order);
    }

    @Override
    public int getSetOrderNum(){
        return this.setOrderMapper.selectSetOrderNum();
    }


}
