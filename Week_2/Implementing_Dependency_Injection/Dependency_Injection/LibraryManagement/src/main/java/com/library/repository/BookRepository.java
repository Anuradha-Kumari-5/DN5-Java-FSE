package com.library.repository;

import java.util.Arrays;
import java.util.List;

/**
 * Repository layer -- the piece that would, in a real application, talk
 * to a database. Here it just returns an in-memory, hardcoded list of
 * book titles, since this exercise is about wiring Spring's IoC
 * container together, not about real persistence (that's what the
 * orm-learn / Spring Data JPA exercise covers separately).
 *
 * Notice this class has NO Spring annotations at all -- no @Repository,
 * no @Component. That's deliberate: in classic XML-based configuration,
 * a class doesn't need to know it's going to be managed by Spring. All
 * of that wiring lives entirely in applicationContext.xml instead.
 */
public class BookRepository {

    public List<String> findAllBooks() {
        return Arrays.asList("The Hobbit", "1984", "Clean Code", "Effective Java");
    }

}
