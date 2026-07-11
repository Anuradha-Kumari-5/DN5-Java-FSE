package com.cognizant.springlearn.service;

import com.cognizant.springlearn.model.Country;

/**
 * Service layer contract for country lookups.
 *
 * Splitting this out from CountryController matters for the same reason
 * a restaurant separates "taking the order" (controller) from "cooking
 * the dish" (service): the controller's job is just to translate HTTP
 * in and out, while the actual lookup logic -- searching the country
 * list, matching case-insensitively -- belongs in the service, where it
 * can be reused, unit-tested, or swapped out (e.g. for a database-backed
 * implementation later) without touching the controller at all.
 */
public interface CountryService {

    /**
     * Finds a country by its ISO code, matched case-insensitively.
     *
     * @param code the country code to search for, e.g. "in", "IN", "In"
     * @return the matching Country
     */
    Country getCountry(String code);

}
