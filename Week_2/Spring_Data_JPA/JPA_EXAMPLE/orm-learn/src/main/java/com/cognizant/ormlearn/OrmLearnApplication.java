package com.cognizant.ormlearn;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.cognizant.ormlearn.model.Country;
import com.cognizant.ormlearn.service.CountryService;

/**
 * @SpringBootApplication is a convenience annotation that bundles three
 * others:
 *   - @Configuration      -- this class can declare beans
 *   - @EnableAutoConfiguration -- Spring Boot auto-configures beans
 *     (like the DataSource, EntityManagerFactory, and JPA repositories)
 *     based on what's on the classpath -- e.g. seeing spring-boot-starter-
 *     data-jpa + a MySQL driver here is what causes Spring to wire up a
 *     working database connection and JPA setup without any of that
 *     configuration being written by hand.
 *   - @ComponentScan      -- Spring scans this package and sub-packages
 *     for @Component/@Service/@Repository/@Controller-annotated classes
 *     and registers them as beans (which is how CountryService and
 *     CountryRepository get found and wired up automatically).
 */
@SpringBootApplication
public class OrmLearnApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrmLearnApplication.class);

    private static CountryService countryService;

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(OrmLearnApplication.class, args);
        LOGGER.info("Inside main");

        countryService = context.getBean(CountryService.class);

        testGetAllCountries();
    }

    private static void testGetAllCountries() {
        LOGGER.info("Start");
        List<Country> countries = countryService.getAllCountries();
        LOGGER.debug("countries={}", countries);
        LOGGER.info("End");
    }

}
