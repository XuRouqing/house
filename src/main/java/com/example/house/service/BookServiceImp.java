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

    @Override
    public List<Book> selectBook(){
        return bookMapper.selectBook();
    }

    @Override
    public List<Book> selectBookByDesignerId(int designerId){
        return bookMapper.selectBookByDesignerId(designerId);
    }

    @Override
    public Book selectBookById(int id){
        return bookMapper.selectBookById(id);
    }

    @Override
    public List<String> selectBookTimeByDesignerId(int designerId){
        return bookMapper.selectBookTimeByDesignerId(designerId);
    }

    @Override
    public void updateBookStatus(int id, int status) {
        this.bookMapper.updateBookStatus(id, status);
    }

    @Override
    public void deleteBook(int id){
        bookMapper.deleteBook(id);
    }

    @Override
    public void modifyBook(Book book){
        bookMapper.modifyBook(book);
    }


}
