package com.example.library.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookDTO {
    private String id;

    private String name;

    private String author;

    private Long pages;

    private Long year;

    private Long copy;
}
