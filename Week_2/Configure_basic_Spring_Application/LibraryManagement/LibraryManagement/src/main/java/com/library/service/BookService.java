package com.library.service;

import java.util.List;

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

    private BookRepository bookRepository;

    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<String> getAllBooks() {
        return bookRepository.findAllBooks();
    }

}
