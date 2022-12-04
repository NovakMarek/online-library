package com.example.library.repository;

import com.example.library.model.Borrowed;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BorrowedRepository extends MongoRepository<Borrowed, String> {
    Borrowed findByUserIdAndBookId(String userId, String bookId);
}
