package com.example.library.service.interfaces;


import com.example.library.model.Book;
import com.example.library.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Book createBook(Book newBook);
    Book findBookById(String id);
    Page<Book> findAll(Pageable page);
    void deleteById(String id);
    Book updateBook(Book bookDetails, Book old);
    User assignBookToUser(String id);
    User returnBookFromUser(String id);
    Book saveBook(Book book);
    List<Book> filterBooks(String name, String  author, Long year);

}
