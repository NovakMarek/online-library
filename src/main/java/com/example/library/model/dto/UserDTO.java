package com.example.library.model.dto;

import com.example.library.model.Book;
import com.example.library.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private String id;

    private String firstName;

    private String lastName;

    private String username;

    private Role role;

    private Set<Book> activeBooks;
}
