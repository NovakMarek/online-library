package com.example.library.controller;

import com.example.library.model.User;
import com.example.library.model.dto.UserDTO;
import com.example.library.model.dto.UserDTOClean;
import com.example.library.model.dto.UserDTOCreate;
import com.example.library.service.interfaces.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public UserDTOClean createUser(@RequestBody UserDTOCreate newUser) {
        logger.info("Request - create User");
        return convertToDtoClean(userService.createUser(convertToEntityCreate(newUser)));
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) {
        User userByID = userService.findUserById(id);

        logger.info("Request - get user with id " + id);
        return userByID;
    }

    @GetMapping
    public Page<UserDTO> all(Pageable page) {
        logger.info("Request - get all Users");
        return userService.findAll(page).map(this::convertToDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        logger.info("Request to delete user with id " + id);
        userService.deleteById(id);
    }

    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable(value = "id") String userId,
                              @RequestBody UserDTOCreate userDetails) {
        User user = userService.findUserById(userId);

        logger.info("Request - update user with id " + userId);
        return convertToDto(userService.updateUser(convertToEntityCreate(userDetails), user));
    }

    @GetMapping("/filter")
    public List<User> filterUsers(@PathParam(value = "firstName") String firstName,
                                @PathParam(value = "lastName") String lastName,
                                @PathParam(value = "birthNumber") String birthNumber){
        return userService.filterUsers(firstName, lastName, birthNumber);
    }

    private UserDTO convertToDto(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    private UserDTOClean convertToDtoClean(User user) {
        return modelMapper.map(user, UserDTOClean.class);
    }

    private User convertToEntityCreate(UserDTOCreate userDTO) {

        try {
            return modelMapper.map(userDTO, User.class);

        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            throw new IllegalArgumentException("User with username: " + userDTO.getUsername() + " cannot be created. ");
        }
    }

}
