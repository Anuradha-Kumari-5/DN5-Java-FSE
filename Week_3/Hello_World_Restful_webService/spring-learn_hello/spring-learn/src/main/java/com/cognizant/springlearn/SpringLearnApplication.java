package com.cognizant.springlearn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the spring-learn application.
 * Running this class starts an embedded Tomcat server (configured on port
 * 8083 via application.properties) and boots up the Spring context,
 * which in turn registers HelloController and its /hello endpoint.
 */
@SpringBootApplication
public class SpringLearnApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringLearnApplication.class, args);
    }

}
