package com.example.library.model.dto;

import com.example.library.model.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTOCreate {
    private String firstName;

    private String lastName;

    private String username;

    private String password;

    private Set<Book> activeBooks = new HashSet<>();
}
