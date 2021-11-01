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
    List<User> selectUserAll();
    public User findUserByNameAndPWD(@Param("name") String name, @Param("password") String password);
    public User findUserByName(@Param("name") String name);
    public void modifyUser(User user);
    public void deleteUser(Integer id);
}
