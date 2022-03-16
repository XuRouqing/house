package com.example.house.service;

import com.example.house.pojo.Book;

import java.util.List;

public interface BookService {
    public void addBook(Book book);
    public List<Book> selectBook();
    public List<Book> selectBookByDesignerId(int designerId);
    public List<Book> selectBookByCustomerId(int id);
    public Book selectBookById(int id);
    public List<String> selectBookTimeByDesignerId(int designerId);
    void updateBookStatus(int id, int status);
    public void modifyBook(Book book);
    public void deleteBook(int id);

}
