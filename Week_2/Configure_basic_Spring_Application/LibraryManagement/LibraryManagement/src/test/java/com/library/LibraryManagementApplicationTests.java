package com.library;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.library.service.BookService;

class LibraryManagementApplicationTests {

    @Test
    void contextLoadsAndWiresBookServiceCorrectly() {
        try (ClassPathXmlApplicationContext context =
                     new ClassPathXmlApplicationContext("applicationContext.xml")) {

            BookService bookService = context.getBean("bookService", BookService.class);
            assertNotNull(bookService, "bookService bean should be present in the context");

            List<String> books = bookService.getAllBooks();
            assertFalse(books.isEmpty(), "BookRepository should have returned some books");
            assertEquals(4, books.size());
        }
    }

}
