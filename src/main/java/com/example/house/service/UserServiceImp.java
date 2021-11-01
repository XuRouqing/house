package com.example.house.service;


import com.example.house.dao.UserMapper;
import com.example.house.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImp implements UserService {


    @Autowired
    private UserMapper userMapper;
    @Override
    public void addUser(User user) {
        this.userMapper.insertUser(user);
    }

    @Override
    public User findUserById(Integer id){
        return userMapper.selectUserById(id);
    }

    @Override
    public List<User> getUserList(){
        return userMapper.selectUserAll();
    }

    @Override
    public boolean login(String name, String password){
        return this.userMapper.findUserByNameAndPWD(name, password)==null?false:true;
    }

    @Override
    public User findUserByNameAndPWD(String name,String password){
        return userMapper.findUserByNameAndPWD(name,password);
    }

    @Override
    public User findUserByName(String name){
        return userMapper.findUserByName(name);
    }

    @Override
    public void modifyUser(User user){
        userMapper.modifyUser(user);
    }

    @Override
    public void deleteUser(Integer id){
        userMapper.deleteUser(id);
    }

}
