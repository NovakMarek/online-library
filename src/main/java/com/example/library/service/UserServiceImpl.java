package com.example.library.service;

import com.example.library.exception.ApiRequestException;
import com.example.library.model.Book;
import com.example.library.model.Role;
import com.example.library.model.User;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository repository;

    private final BookService bookService;

    private final MongoTemplate mongoTemplate;

    public UserServiceImpl(@Lazy UserRepository repository, @Lazy BookService bookService, MongoTemplate mongoTemplate){
        this.repository = repository;
        this.bookService = bookService;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public User currentUser(){
        User user = new User("2", "bill","Bill", "Gates", "password123", "9822124512",Role.USER, Boolean.TRUE, 0, new HashSet<>(), new ArrayList<>());
        return user;
    }

    @Override
    @Transactional
    public User createUser(User newUser){
        try{
            newUser.setRole(Role.USER);
            newUser.setValidAccount(Boolean.FALSE);
            logger.info("User with id " + newUser.getId() + " has been created.");
            return repository.insert(newUser);
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new IllegalArgumentException("User with user name " + newUser.getUsername() + " has forbidden data");
        }
    }

    @Override
    @Transactional
    public User findUserById(String id) {
        return repository.findById(id).orElseThrow(() -> new ApiRequestException("User with id: " + id + " was not found."));
    }

    @Override
    @Transactional
    public Page<User> findAll(Pageable page) {
        return repository.findAll(page);
    }

    @Override
    @Transactional
    public void deleteById(String id) {

        User user = repository.findById(id).orElseThrow(() -> new ApiRequestException("User with id: " + id + " was not found."));;

        logger.info("Deleting user with id " + id);
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public User updateUser(User userDetails, User old) {

        if (userDetails.getFirstName() != null)
            old.setFirstName(userDetails.getFirstName());

        if (userDetails.getLastName() != null)
            old.setLastName(userDetails.getLastName());

        if (userDetails.getUsername() != null)
            old.setUsername(userDetails.getUsername());

        if (userDetails.getPassword() != null)
            old.setPassword(userDetails.getPassword());
        if (userDetails.getBirthNumber() != null)
            old.setBirthNumber(userDetails.getBirthNumber());
        if(userDetails.getValidAccount() != null)
            old.setValidAccount(userDetails.getValidAccount());

        Set<String> newBooks = new HashSet<>();
        Set<String> oldBooks = new HashSet<>();


        userDetails.getActiveBooks().forEach(b -> {
            Book book = bookService.findBookById(b.getId());

            if(old.getValidAccount() == Boolean.FALSE){
                logger.error("Invalid user account.");
                throw new ApiRequestException("Invalid user account.");
            }

            if(book.getCopy() == 0){
                logger.error("This book is not available.");
                throw new ApiRequestException("This book is not available.");
            }

            if(old.getNumberOfBooks() == 5){
                logger.warn("This user already borrowed 6 books which is the max number of books he can borrow.");
                throw new ApiRequestException("This user already borrowed 6 books which is the max number of books he can borrow.");
            }

            if(!old.getActiveBooks().contains(book)){

                Long numberOfCopies = book.getCopy() - 1;

                book.setCopy(numberOfCopies);

                bookService.saveBook(book);

                int newNumberOfBooks = old.getActiveBooks().size();
                old.setNumberOfBooks(newNumberOfBooks);

                //old.addBook( bookService.findBookById(book.getId()));
                Book newBook = bookService.findBookById(book.getId());
                newBooks.add(newBook.getId());

            } else{
                Book oldBook = bookService.findBookById(book.getId());
                oldBooks.add(oldBook.getId());
            }
        });

        //Add +1 copy to book that will be returned
        old.getActiveBooks().forEach(i -> {
            if(!oldBooks.contains(i.getId())){
                i.setCopy(i.getCopy() + 1);
                bookService.saveBook(i);
            }
        });

        old.getActiveBooks().clear();

        oldBooks.forEach(oldB -> {
            Book oldBook = bookService.findBookById(oldB);
            old.addBook(oldBook);
        });

        newBooks.forEach(newB -> {
            Book newBook = bookService.findBookById(newB);
            old.addBook(newBook);
            old.getHistoryBooks().add(newBook);
        });

        try {
            logger.info("User with id " + old.getId() + " has been updated");
            return repository.save(old);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new IllegalArgumentException("User with id " + old.getId() + " has forbidden data");
        }
    }

    @Override
    public User save(User user){
        return repository.save(user);
    }

    @Override
    public List<User> filterUsers(String firstName, String lastName, String birthNumber){
        Criteria criteria = new Criteria();
        if (firstName != null && firstName.length() >= 3) {
            criteria = criteria.and("firstName").regex(firstName);
        }
        if (lastName != null && lastName.length() >= 3) {
            criteria = criteria.and("lastName").regex(lastName);
        }
        if (birthNumber != null && birthNumber.length() >= 3) {
            criteria = criteria.and("birthNumber").regex(birthNumber);
        }

        Query query = new Query(criteria);
        List<User> users = mongoTemplate.find(query, User.class);
        return users;
    }
}
