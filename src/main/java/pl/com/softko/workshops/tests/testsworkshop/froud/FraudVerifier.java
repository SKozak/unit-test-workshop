package pl.com.softko.workshops.tests.testsworkshop.froud;

import pl.com.softko.workshops.tests.testsworkshop.loan.LoanOrderClient;

public interface FraudVerifier {
    boolean isFraud(LoanOrderClient client);
}
