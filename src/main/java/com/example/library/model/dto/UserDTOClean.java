package com.example.library.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTOClean {
    private String id;

    private String firstName;

    private String lastName;

    private String username;

    private String role;
}
