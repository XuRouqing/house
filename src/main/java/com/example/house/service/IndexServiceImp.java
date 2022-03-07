package com.example.house.service;

import com.example.house.dao.BookMapper;
import com.example.house.dao.IndexMapper;
import com.example.house.pojo.Book;
import com.example.house.pojo.Index;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IndexServiceImp implements IndexService  {
    @Autowired
    private IndexMapper indexMapper;

    @Override
    public void addDesignerlevel(Index index){
        indexMapper.addDesignerlevel(index);
    }

    @Override
    public void addHousearea(Index index){
        indexMapper.addHousearea(index);
    }

    @Override
    public void addHouseform(Index index){
        indexMapper.addHouseform(index);
    }

    @Override
    public void addHousetype(Index index){
        indexMapper.addHousetype(index);
    }

    @Override
    public void addStyleindex(Index index){
        indexMapper.addStyleindex(index);
    }

    @Override
    public void addworkertype(Index index){
        indexMapper.addworkertype(index);
    }

    @Override
    public void modifyDesignerlevel(Index index){
        indexMapper.modifyDesignerlevel(index);
    }

    @Override
    public void modifyHousearea(Index index){
        indexMapper.modifyHousearea(index);
    }

    @Override
    public void modifyHouseform(Index index){
        indexMapper.modifyHouseform(index);
    }

    @Override
    public void modifyHousetype(Index index){
        indexMapper.modifyHousetype(index);
    }

    @Override
    public void modifyStyleindex(Index index){
        indexMapper.modifyStyleindex(index);
    }

    @Override
    public void modifyworkertype(Index index){
        indexMapper.modifyworkertype(index);
    }

    @Override
    public void delDesignerlevel(int id){
        indexMapper.delDesignerlevel(id);
    }

    @Override
    public void delHousearea(int id){
        indexMapper.delHousearea(id);
    }

    @Override
    public void delHouseform(int id){
        indexMapper.delHouseform(id);
    }

    @Override
    public void delHousetype(int id){
        indexMapper.delHousetype(id);
    }

    @Override
    public void delStyleindex(int id){
        indexMapper.delStyleindex(id);
    }

    @Override
    public void delworkertype(int id){
        indexMapper.delworkertype(id);
    }
}
