package com.library;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.library.service.BookService;

/**
 * Loads applicationContext.xml, retrieves the fully-wired BookService
 * bean from it, and calls a method on it -- proving the whole chain
 * (XML config -> BookRepository bean -> BookService bean with
 * BookRepository injected in) actually works end to end.
 */
public class LibraryManagementApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(LibraryManagementApplication.class);

    public static void main(String[] args) {
        LOGGER.info("START - loading applicationContext.xml");

        // ClassPathXmlApplicationContext looks for applicationContext.xml
        // on the classpath -- which, for a Maven project, means
        // src/main/resources/applicationContext.xml (copied to
        // target/classes at build time).
        try (ClassPathXmlApplicationContext context =
                     new ClassPathXmlApplicationContext("applicationContext.xml")) {

            // Ask the container for the "bookService" bean. Spring
            // returns the same fully-constructed, dependency-injected
            // BookService instance it built when the context started up.
            BookService bookService = context.getBean("bookService", BookService.class);

            // Step 3 of this exercise is specifically about VERIFYING
            // dependency injection, not just using it indirectly -- so
            // check explicitly that BookRepository was actually injected
            // before relying on it.
            if (bookService.getBookRepository() != null) {
                LOGGER.info("Dependency Injection verified: BookService.bookRepository is populated");
            } else {
                LOGGER.error("Dependency Injection FAILED: BookService.bookRepository is null");
            }

            List<String> books = bookService.getAllBooks();
            LOGGER.info("Books retrieved via BookService -> BookRepository: {}", books);
        }

        LOGGER.info("END - context closed");
    }

}
