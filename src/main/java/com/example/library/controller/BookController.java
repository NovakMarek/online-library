package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.model.User;
import com.example.library.service.interfaces.BookService;
import com.example.library.service.interfaces.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final BookService bookService;
    private final ModelMapper modelMapper;

    public BookController(UserService userService, ModelMapper modelMapper, BookService bookService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.bookService = bookService;
    }

    @PutMapping("/{id}")
    public User assignBookToUser(@PathVariable String id){
        return bookService.assignBookToUser(id);
    }

    @PutMapping("/return/{id}")
    public User returnBookFromUser(@PathVariable String id){
        return bookService.returnBookFromUser(id);
    }

    @PostMapping
    public Book createBook(@RequestBody Book newBook){
        return bookService.createBook(newBook);
    }

    @GetMapping("/multi")
    public List<Book> filterBooks(
            @PathParam(value = "name") String name,
            @PathParam(value = "author") String author,
            @PathParam(value = "year") Long year
    ){
        return bookService.filterBooks(name, author, year);
    }

}
