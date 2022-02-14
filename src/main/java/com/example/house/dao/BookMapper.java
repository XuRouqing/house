package com.example.house.dao;

import com.example.house.pojo.Book;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;


@Component
@Mapper
public interface BookMapper {
    public void insertBook(Book book);
}
