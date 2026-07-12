package com.cognizant.loan.controller;

import com.cognizant.loan.model.Loan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoanController {

    private static final Logger logger = LoggerFactory.getLogger(LoanController.class);

    /**
     * Handles GET requests to /loans/{number}.
     *
     * No database or backend behind this either -- a dummy endpoint.
     * The "number" requested is echoed back via @PathVariable; the rest
     * of the fields are fixed, matching the exercise's sample response.
     *
     * @param number the loan account number from the URL path
     * @return a dummy Loan, serialized to JSON
     */
    @GetMapping("/loans/{number}")
    public Loan getLoan(@PathVariable String number) {
        logger.info("START - getLoan({})", number);

        Loan loan = new Loan(number, "car", 400000, 3258, 18);

        logger.info("END - getLoan({})", number);
        return loan;
    }

}
