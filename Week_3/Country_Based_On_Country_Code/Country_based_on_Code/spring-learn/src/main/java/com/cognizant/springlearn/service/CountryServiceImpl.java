package com.cognizant.springlearn.service;

import com.cognizant.springlearn.model.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Default implementation of CountryService.
 *
 * The "countryList" bean it depends on is not built here -- it's defined
 * declaratively in country.xml (loaded via @ImportResource on
 * SpringLearnApplication) and injected in by Spring, the same pattern
 * CountryController already uses for the single "india" bean, just
 * scaled up to a list.
 */
@Service
public class CountryServiceImpl implements CountryService {

    private static final Logger logger = LoggerFactory.getLogger(CountryServiceImpl.class);

    private final List<Country> countryList;

    public CountryServiceImpl(@Qualifier("countryList") List<Country> countryList) {
        this.countryList = countryList;
    }

    @Override
    public Country getCountry(String code) {
        logger.info("START - getCountry({})", code);

        // Case-insensitive lookup using a lambda passed to Stream.filter():
        // equalsIgnoreCase() means "in", "IN", and "In" are all treated
        // as the same match against the stored code (e.g. "IN").
        Country match = countryList.stream()
                .filter(country -> country.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No country found for code: " + code));

        logger.info("END - getCountry({})", code);
        return match;
    }

}
