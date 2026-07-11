package com.cognizant.springlearn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * Entry point for the spring-learn application.
 * Running this class starts an embedded Tomcat server (configured on port
 * 8083 via application.properties) and boots up the Spring context,
 * which in turn registers HelloController (/hello) and CountryController
 * (/country).
 *
 * @ImportResource tells Spring Boot's (mostly annotation-driven) context
 * to also load bean definitions from the given classic XML file, so the
 * "india" bean defined in beans.xml becomes available for lookup/injection
 * just like any @Component-annotated bean would be.
 */
@SpringBootApplication
@ImportResource("classpath:beans.xml")
public class SpringLearnApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringLearnApplication.class, args);
    }

}
