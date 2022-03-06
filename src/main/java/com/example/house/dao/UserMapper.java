package com.example.house.dao;

import com.example.house.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface UserMapper {
    void insertUser(User user);
    User selectUserById(int id);
    int selectUserNum();
    List<User> selectUserAll();
    public User findUserByCodeAndPWD(@Param("code") String code, @Param("password") String password);
    public User findUserByCode(@Param("code") String code);
    public void modifyUser(User user);
    public void modifyPassword(int id,String password);
    public void deleteUser(Integer id);
    public int checkCode(String code);
}
