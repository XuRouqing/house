package com.example.house.service;

import com.example.house.pojo.Set;
import com.example.house.pojo.SetOrder;

import java.util.List;

public interface SetOrderService {
    public void addOrder(SetOrder order);
    public List<SetOrder> getSetOrderAll();
    public SetOrder selectSetOrderById(int id);
    public List<SetOrder> selectSetOrderByCustomerId(int id);
    public void updateSetOrderStatus(int id, int status);
    public void delSetOrder(int id);
    public int getSetOrderNum();
}
