package com.cognizant.ormlearn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cognizant.ormlearn.model.Country;

/**
 * Spring Data JPA repository for Country.
 *
 * Extending JpaRepository<Country, String> (String = the type of
 * Country's primary key) is all it takes to get findAll(), findById(),
 * save(), delete(), and more -- Spring generates the implementation at
 * runtime; there's no method body to write here.
 *
 * @Repository is technically optional on interfaces that extend
 * JpaRepository (Spring detects and proxies them automatically either
 * way), but it's included here as the assignment specifies, and it
 * doesn't hurt -- it also makes Spring translate any database
 * exceptions this repository throws into Spring's own consistent
 * DataAccessException hierarchy.
 */
@Repository
public interface CountryRepository extends JpaRepository<Country, String> {

}
