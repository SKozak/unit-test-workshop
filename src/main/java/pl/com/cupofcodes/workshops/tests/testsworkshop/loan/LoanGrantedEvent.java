package pl.com.cupofcodes.workshops.tests.testsworkshop.loan;

import lombok.Value;
import pl.com.cupofcodes.workshops.tests.testsworkshop.Event;

import java.util.UUID;

@Value
class LoanGrantedEvent extends Event {
    private final UUID loanId;
}
