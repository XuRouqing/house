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
    public int getUserNum(){
        return userMapper.selectUserNum();
    }

    @Override
    public List<User> getUserList(){
        return userMapper.selectUserAll();
    }

    @Override
    public boolean login(String code, String password){
        return this.userMapper.findUserByCodeAndPWD(code, password)==null?false:true;
    }

    @Override
    public User findUserByCodeAndPWD(String code,String password){
        return userMapper.findUserByCodeAndPWD(code,password);
    }

    @Override
    public User findUserByCode(String code){
        return userMapper.findUserByCode(code);
    }

    @Override
    public void modifyUser(User user){
        userMapper.modifyUser(user);
    }

    @Override
    public void modifyPassword(int id, String password){
        userMapper.modifyPassword(id,password);
    }

    @Override
    public void deleteUser(Integer id){
        userMapper.deleteUser(id);
    }

    @Override
    public int checkCode(String id){
        return userMapper.checkCode(id);
    }

}
