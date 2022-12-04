package com.example.library.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Borrowed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String userId;

    private String bookId;

    private LocalDate expire;

    public Borrowed(String userId, String bookId, LocalDate expire){
        this.userId = userId;
        this.bookId = bookId;
        this.expire = expire;
    }

}
