package pl.com.softko.workshops.tests.testsworkshop.loan;

import lombok.Value;
import pl.com.softko.workshops.tests.testsworkshop.Event;

import java.util.UUID;

@Value
class LoanGrantedEvent extends Event {
    private final UUID loanId;
}
