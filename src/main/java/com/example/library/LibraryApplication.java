package com.example.library;

import com.example.library.model.Book;
import com.example.library.model.Role;
import com.example.library.model.User;
import com.example.library.repository.BookRepository;
import com.example.library.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.HashSet;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class LibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(UserRepository userRepository, BookRepository bookRepository) {
		return args -> {
			User user = new User("1", "admin", "Admin", "Admin", "rootpass", Role.ADMIN);
			User user1 = new User("2", "bill", "Bill", "Gates","password123", "872343989", Role.USER, Boolean.TRUE, 0, new HashSet<>(), new ArrayList<>());
			User user2 = new User("3", "elon", "Elon", "Musk","password123", "6727976789", Role.USER, Boolean.TRUE, 0, new HashSet<>(), new ArrayList<>());

			Book book = new Book("1", "Harry Potter", "Rowling", 302L, 1998L,40L);
			Book book1 = new Book("2", "Gatsby", "Fitzgerald", 455L, 1928L,40L);
			Book book2 = new Book("3", "LOTR", "Rowling", 400L, 1948L,40L);
			Book book3 = new Book("4", "Kytice", "Erben", 137L, 1853L,40L);
			Book book4 = new Book("5", "Lakomec", "Rowling", 265L, 1668L,40L);

			userRepository.insert(user);
			userRepository.insert(user1);
			userRepository.insert(user2);

			bookRepository.insert(book);
			bookRepository.insert(book1);
			bookRepository.insert(book2);
			bookRepository.insert(book3);
			bookRepository.insert(book4);
		};
	}


}
