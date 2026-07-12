package com.cognizant.account.controller;

import com.cognizant.account.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    /**
     * Handles GET requests to /accounts/{number}.
     *
     * There's no database or backend behind this service -- it's a
     * dummy endpoint. The "number" the caller asked for is echoed back
     * (via @PathVariable) so the response at least reflects what was
     * requested; "type" and "balance" are fixed values, matching the
     * sample response given in the exercise.
     *
     * @param number the account number from the URL path
     * @return a dummy Account, serialized to JSON
     */
    @GetMapping("/accounts/{number}")
    public Account getAccount(@PathVariable String number) {
        logger.info("START - getAccount({})", number);

        Account account = new Account(number, "savings", 234343);

        logger.info("END - getAccount({})", number);
        return account;
    }

}
