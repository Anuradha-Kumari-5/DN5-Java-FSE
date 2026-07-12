package com.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.library.repository.BookRepository;

/**
 * Service layer. Like BookRepository, this class has no Spring
 * annotations -- it's a plain Java class ("POJO"). The bookRepository
 * field is set via a setter method, because applicationContext.xml
 * wires it in using setter (property) injection:
 *
 *   <bean id="bookService" class="com.library.service.BookService">
 *       <property name="bookRepository" ref="bookRepository"/>
 *   </bean>
 *
 * When Spring builds the "bookService" bean, it instantiates this class
 * with a no-arg constructor, then calls setBookRepository(...), passing
 * in the already-built "bookRepository" bean. This is the XML-config
 * equivalent of what @Autowired does automatically in annotation-based
 * Spring.
 */
public class BookService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookService.class);

    private BookRepository bookRepository;

    public void setBookRepository(BookRepository bookRepository) {
        // Logging here makes the injection observable: this line prints
        // during context startup, before main() runs any of its own
        // code, proving Spring -- not your application code -- is the
        // one calling this setter.
        LOGGER.info("Dependency injected: BookRepository -> BookService");
        this.bookRepository = bookRepository;
    }

    /**
     * Exposed purely so tests and the main class can verify injection
     * actually happened (i.e. this isn't null) without needing to
     * exercise getAllBooks() to find out indirectly.
     */
    public BookRepository getBookRepository() {
        return bookRepository;
    }

    public List<String> getAllBooks() {
        return bookRepository.findAllBooks();
    }

}
