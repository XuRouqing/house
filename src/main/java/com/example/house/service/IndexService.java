package com.example.house.service;


import com.example.house.pojo.Index;

public interface IndexService {
    public void addDesignerlevel(Index index);
    public void addHousearea(Index index);
    public void addHouseform(Index index);
    public void addHousetype(Index index);
    public void addStyleindex(Index index);
    public void addworkertype(Index index);
    public void modifyDesignerlevel(Index index);
    public void modifyHousearea(Index index);
    public void modifyHouseform(Index index);
    public void modifyHousetype(Index index);
    public void modifyStyleindex(Index index);
    public void modifyworkertype(Index index);
    public void delDesignerlevel(int id);
    public void delHousearea(int id);
    public void delHouseform(int id);
    public void delHousetype(int id);
    public void delStyleindex(int id);
    public void delworkertype(int id);
}
