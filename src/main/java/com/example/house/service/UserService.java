package com.example.house.service;

import com.example.house.pojo.User;

import java.util.List;

public interface UserService {
    public void addUser(User user);
    public User findUserById(Integer id);
    public List<User> getUserList();
    public boolean login(String name, String password);
    public User findUserByNameAndPWD(String name,String password);
    public User findUserByName(String name);
    public void modifyUser(User user);
    public void deleteUser(Integer id);


}
