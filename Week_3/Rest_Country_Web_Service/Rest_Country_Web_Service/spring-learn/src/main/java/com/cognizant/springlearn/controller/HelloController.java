package com.cognizant.springlearn.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller that exposes a single "Hello World" endpoint.
 *
 * @RestController is a convenience annotation that combines @Controller
 * and @ResponseBody -- it tells Spring that every method's return value
 * should be written directly into the HTTP response body (as plain text
 * or JSON, depending on type), rather than being resolved to a view
 * (e.g. a JSP or HTML page).
 */
@RestController
public class HelloController {

    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    /**
     * Handles GET requests to /hello.
     *
     * @return the hardcoded greeting "Hello World!!"
     */
    @GetMapping("/hello")
    public String sayHello() {
        logger.info("START - sayHello()");

        String greeting = "Hello World!!";

        logger.info("END - sayHello()");
        return greeting;
    }

}
