package com.example.house.dao;

import com.example.house.pojo.Book;
import com.example.house.pojo.Room;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Mapper
public interface BookMapper {
    public void insertBook(Book book);
    public List<Book> selectBook();
    public List<Book> selectBookByDesignerId(int designerId);
    public List<Book> selectBookByCustomerId(int id);
    public Book selectBookById(int id);
    public List<String> selectBookTimeByDesignerId(int designerId);
    void updateBookStatus(int id, int status);
    public void modifyBook(Book book);
    public void deleteBook(int id);

}
