package com.cognizant.springlearn.controller;

import com.cognizant.springlearn.model.Country;
import com.cognizant.springlearn.service.CountryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Autowired
    private CountryService countryService;

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

    /**
     * Handles GET requests to /countries/{code}, e.g. /countries/in.
     *
     * @PathVariable extracts the {code} segment of the URL and binds it
     * to the "code" method parameter. The actual case-insensitive
     * lookup logic lives in CountryService, not here -- this method's
     * only job is to receive the HTTP request and hand off to the
     * service layer.
     *
     * @param code the country code from the URL path, e.g. "in"
     * @return the matching Country, later serialized to JSON
     */
    @GetMapping("/countries/{code}")
    public Country getCountry(@PathVariable String code) {
        logger.info("START - getCountry({})", code);

        Country result = countryService.getCountry(code);

        logger.info("END - getCountry({})", code);
        return result;
    }

}
