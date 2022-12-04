package com.example.library.model;

import lombok.*;

import org.springframework.data.annotation.Id;
import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @NonNull
    private String name;

    @NonNull
    private String author;

    @NonNull
    private Long pages;

    @NonNull
    private Long year;

    @NonNull
    private Long copy;

    public Book(String id, String name){
        this.id = id;
        this.name = name;
    }
}
