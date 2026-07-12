package com.cognizant.ormlearn.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cognizant.ormlearn.model.Country;
import com.cognizant.ormlearn.repository.CountryRepository;

/**
 * Service layer sitting between the (not-yet-built) controller and the
 * repository. Even though getAllCountries() is a thin pass-through to
 * countryRepository.findAll() today, keeping this layer separate is
 * what lets business logic (validation, combining multiple repository
 * calls, etc.) be added later without touching the repository or any
 * caller of this service.
 */
@Service
public class CountryService {

    @Autowired
    private CountryRepository countryRepository;

    /**
     * @Transactional here matters for a subtle reason: JPA entities
     * fetched inside a transaction stay "attached" to the persistence
     * context for as long as that transaction is open, which is what
     * lets lazy-loaded fields (not used in this simple Country entity,
     * but common in richer ones) be accessed safely. Without it, you
     * can hit LazyInitializationException once the session closes.
     */
    @Transactional
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

}
