package com.example.house.service;

import com.example.house.pojo.User;

import java.util.List;

public interface UserService {
    public void addUser(User user);
    public User findUserById(Integer id);
    public List<User> getUserList();
    public boolean login(String code, String password);
    public User findUserByCodeAndPWD(String code,String password);
    public User findUserByCode(String code);
    public void modifyUser(User user);
    public void modifyPassword(User user);
    public void deleteUser(Integer id);


}
