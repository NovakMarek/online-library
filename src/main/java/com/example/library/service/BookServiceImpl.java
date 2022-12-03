package com.example.library.service;

import com.example.library.controller.UserController;
import com.example.library.exception.ApiRequestException;
import com.example.library.model.Book;
import com.example.library.model.User;
import com.example.library.repository.BookRepository;
import com.example.library.repository.UserRepository;
import com.example.library.service.interfaces.BookService;
import com.example.library.service.interfaces.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@EnableScheduling
public class BookServiceImpl implements BookService {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final BookRepository repository;
    private final UserService userService;
    private final UserRepository userRepository;

    private final MongoTemplate mongoTemplate;

    public BookServiceImpl(@Lazy BookRepository repository, @Lazy UserService userService, UserRepository userRepository, MongoTemplate mongoTemplate){this.repository = repository;  this.userService = userService; this.userRepository = userRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    @Transactional
    public Book createBook(Book newBook){
        try{
            logger.info("Book with id " + newBook.getId() + " has been created.");
            return repository.insert(newBook);
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new IllegalArgumentException("Book with title " + newBook.getName() + " has forbidden data");
        }
    }

    @Override
    @Transactional
    public Book findBookById(String id) {
        return repository.findById(id).orElseThrow(() -> new ApiRequestException("Book with id: " + id + " was not found."));
    }

    @Override
    @Transactional
    public Page<Book> findAll(Pageable page) {
        return repository.findAll(page);
    }

    @Override
    @Transactional
    public void deleteById(String id) {

        Book book = repository.findById(id).orElseThrow(() -> new ApiRequestException("Book with id: " + id + " was not found."));

        logger.info("Deleting book with id " + id);
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public Book updateBook(Book bookDetails, Book old) {


        if (bookDetails.getName() != null){
            old.setName(bookDetails.getName());
        }
        if (bookDetails.getYear() != null){
            old.setYear(bookDetails.getYear());
        }
        if (bookDetails.getAuthor() != null){
            old.setAuthor(bookDetails.getAuthor());
        }
        if (bookDetails.getCopy() != null){
            old.setCopy(bookDetails.getCopy());
        }
        if (bookDetails.getPages() != null){
            old.setPages(bookDetails.getPages());
        }

        try {
            logger.info("Book with id " + old.getId() + " has been updated");
            return repository.save(old);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new IllegalArgumentException("Book with id " + old.getId() + " has forbidden data");
        }
    }

    @Override
    public User assignBookToUser(String id){
        Book book = findBookById(id);

        User user = userService.findUserById(userService.currentUser().getId());

        if(user.getValidAccount() == Boolean.FALSE){
            logger.error("Invalid user account.");
            throw new ApiRequestException("Invalid user account.");
        }
        if(book.getCopy() == 0){
            logger.error("This book is not available.");
            throw new ApiRequestException("This book is not available.");
        }

        if(user.getActiveBooks().contains(book)){
            logger.warn("This user already borrowed this book.");
            throw new ApiRequestException("This user already borrowed this book.");
        }
        if(user.getNumberOfBooks() >= 6){
            logger.warn("This user already borrowed 6 books which is the max number of books he can borrow.");
            throw new ApiRequestException("This user already borrowed 6 books which is the max number of books he can borrow.");
        }

        Long numberOfCopies = book.getCopy() - 1;

        book.setCopy(numberOfCopies);


        repository.save(book);

        int newNumberOfBooks = user.getNumberOfBooks() + 1;
        user.setNumberOfBooks(newNumberOfBooks);

        user.getActiveBooks().add(book);
        user.getHistoryBooks().add(book);
        logger.info("User " + user.getUsername() + " borrowed book.");

        return userService.save(user);
    }

    @Override
    public User returnBookFromUser(String id){
        Book book = findBookById(id);

        User user = userService.findUserById(userService.currentUser().getId());

        user.getActiveBooks().remove(book);

        Long numberOfCopies = book.getCopy() + 1;

        book.setCopy(numberOfCopies);

        repository.save(book);

        int newNumberOfBooks = user.getNumberOfBooks() - 1;
        user.setNumberOfBooks(newNumberOfBooks);

        logger.info("User " + user.getUsername() + " returned book.");

        return userService.save(user);
    }

    @Override
    public Book saveBook(Book book){
        return repository.save(book);
    }

    @Override
    public List<Book> filterBooks(String name, String  author, Long year){
        Criteria criteria = new Criteria();
        if(name != null && name.length() >= 3){
            criteria = criteria.and("name").regex(name);
        }
        if(author != null && author.length() >= 3){
            criteria = criteria.and("author").regex(author);
        }
        if(year != null && year >= 3){
            criteria = criteria.and("year").is(year);
        }

        Query query = new Query(criteria);
        List<Book> books = mongoTemplate.find(query, Book.class);
        return books;
    }

    @Scheduled(cron = "0 */1 * * * *")
    public void checkExpireDate() {
        Iterable<Book> books = repository.findAll();

        LocalDate now = LocalDate.now();

        StreamSupport.stream(books.spliterator(), false)
                .forEach(x -> {
                    if (now.isEqual(x.getExpire())) {
                        logger.info("Deleting expired book with id" + x.getId());
                    }
                });
    }

}
