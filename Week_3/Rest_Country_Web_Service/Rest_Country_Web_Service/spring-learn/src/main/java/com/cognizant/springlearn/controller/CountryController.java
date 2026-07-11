package com.cognizant.springlearn.controller;

import com.cognizant.springlearn.model.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller that returns India's country details.
 *
 * Unlike HelloController, this one doesn't build its response by hand --
 * it fetches an already-configured "india" bean straight out of the
 * Spring ApplicationContext (that bean was defined in beans.xml and
 * loaded at startup via @ImportResource on SpringLearnApplication) and
 * hands it back. Spring's Jackson message converter then serializes that
 * Country object into JSON before it goes out on the wire.
 */
@RestController
public class CountryController {

    private static final Logger logger = LoggerFactory.getLogger(CountryController.class);

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Handles GET requests to /country.
     * Uses @RequestMapping (rather than the @GetMapping shortcut) as
     * required by the assignment; @RequestMapping without an explicit
     * "method" attribute would accept any HTTP method, so it's scoped
     * here to GET explicitly.
     *
     * @return the India Country bean, later serialized to JSON
     */
    @RequestMapping(value = "/country", method = RequestMethod.GET)
    public Country getCountryIndia() {
        logger.info("START - getCountryIndia()");

        // Look up the bean named "india" (defined in beans.xml) from the
        // Spring context, rather than constructing a new Country object
        // in Java code.
        Country india = applicationContext.getBean("india", Country.class);

        logger.info("END - getCountryIndia()");
        return india;
    }

}
