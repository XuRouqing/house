package com.example.house.service;

import com.example.house.dao.BookMapper;
import com.example.house.dao.CityMapper;
import com.example.house.pojo.Book;
import com.example.house.pojo.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BookServiceImp implements BookService  {
    @Autowired
    private BookMapper bookMapper;

    @Override
    public void addBook(Book book){
        bookMapper.insertBook(book);
    }
}
