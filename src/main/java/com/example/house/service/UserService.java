package com.example.house.service;

import com.example.house.pojo.User;

import java.util.List;

public interface UserService {
    public void addUser(User user);
    public int getUserNum();
    public User findUserById(Integer id);
    public List<User> getUserList();
    public boolean login(String code, String password);
    public User findUserByCodeAndPWD(String code,String password);
    public User findUserByCode(String code);
    public void modifyUser(User user);
    public void modifyUserMain(User user);
    public void modifyPassword(int id,String password);
    public void deleteUser(Integer id);
    public int checkCode(String code);

}
