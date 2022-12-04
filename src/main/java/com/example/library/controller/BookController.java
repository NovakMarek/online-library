package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.model.User;
import com.example.library.model.dto.*;
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

    @PostMapping
    public BookDTOClean createBook(@RequestBody BookDTOCreate newBook) {
        logger.info("Request - create User");
        return convertToDtoClean(bookService.createBook(convertToEntityCreate(newBook)));
    }

    @GetMapping("/{id}")
    public BookDTO getBookById(@PathVariable String id){
        Book bookById = bookService.findBookById(id);

        logger.info("Request - get book with id " + id);
        return convertToDto(bookById);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable String id) {
        logger.info("Request to delete book with id " + id);
        bookService.deleteById(id);
    }

    @PutMapping("/{id}")
    public BookDTO updateBook(@PathVariable(value = "id") String bookId,
                              @RequestBody BookDTOCreate bookDetails) {
        Book book = bookService.findBookById(bookId);

        logger.info("Request - update book with id " + bookId);
        return convertToDto(bookService.updateBook(convertToEntityCreate(bookDetails), book));
    }

    @PutMapping("/assign/{id}")
    public User assignBookToUser(@PathVariable String id){
        return bookService.assignBookToUser(id);
    }

    @PutMapping("/return/{id}")
    public User returnBookFromUser(@PathVariable String id){
        return bookService.returnBookFromUser(id);
    }

    @GetMapping("/filter")
    public List<Book> filterBooks(
            @PathParam(value = "name") String name,
            @PathParam(value = "author") String author,
            @PathParam(value = "year") Long year
    ){
        return bookService.filterBooks(name, author, year);
    }

    private BookDTO convertToDto(Book book) {
        return modelMapper.map(book, BookDTO.class);
    }

    private BookDTOClean convertToDtoClean(Book book){
        return modelMapper.map(book, BookDTOClean.class);
    }

    private Book convertToEntityCreate(BookDTOCreate bookDTO) {

        try {
            return modelMapper.map(bookDTO, Book.class);

        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            throw new IllegalArgumentException("Book with title: " + bookDTO.getName() + " cannot be created. ");
        }
    }

}
