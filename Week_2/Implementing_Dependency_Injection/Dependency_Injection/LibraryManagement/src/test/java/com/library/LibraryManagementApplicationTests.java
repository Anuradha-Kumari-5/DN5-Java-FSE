package com.library;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.library.repository.BookRepository;
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

    /**
     * This is the test that specifically targets Exercise 2's goal:
     * proving dependency injection happened, not just that the end
     * result (getAllBooks()) works. It checks the injected field
     * directly, and also confirms it's the SAME BookRepository instance
     * Spring registered as the "bookRepository" bean -- not some other
     * instance BookService might have constructed on its own.
     */
    @Test
    void bookRepositoryIsInjectedIntoBookService() {
        try (ClassPathXmlApplicationContext context =
                     new ClassPathXmlApplicationContext("applicationContext.xml")) {

            BookService bookService = context.getBean("bookService", BookService.class);
            BookRepository repositoryBean = context.getBean("bookRepository", BookRepository.class);

            assertNotNull(bookService.getBookRepository(),
                    "BookRepository should have been injected via the setter, not left null");
            assertEquals(repositoryBean, bookService.getBookRepository(),
                    "BookService should be holding the exact bookRepository bean from the context, "
                            + "not a separately constructed instance");
        }
    }

}
