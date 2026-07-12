package com.cognizant.ormlearn;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * NOTE: this test starts the full Spring context, which will try to
 * open a real connection to the ormlearn MySQL schema (per
 * application.properties). It will fail if MySQL isn't running
 * locally with that schema/table already created -- run sql/country.sql
 * first, as described in README.md.
 */
@SpringBootTest
class OrmLearnApplicationTests {

    @Test
    void contextLoads() {
    }

}
