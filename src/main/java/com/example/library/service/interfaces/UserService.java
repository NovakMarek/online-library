package com.example.library.service.interfaces;

import com.example.library.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User currentUser();
    User createUser(User newUser);
    User findUserById(String id);
    Page<User> findAll(Pageable page);
    void deleteById(String id);
    User updateUser(User userDetails, User old);
    User save(User user);
    List<User> filterUsers(String firstName, String lastName, String birthNumber);
}
