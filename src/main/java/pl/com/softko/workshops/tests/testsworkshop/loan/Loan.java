package pl.com.softko.workshops.tests.testsworkshop.loan;

import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
class Loan {
    private final UUID id = UUID.randomUUID();
    private final BigDecimal calculatedPriceAfterFess;
    private final Integer numberOfInstallments;
    private final long clientId;
}
