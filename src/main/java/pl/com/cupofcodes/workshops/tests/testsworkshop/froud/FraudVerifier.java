package pl.com.cupofcodes.workshops.tests.testsworkshop.froud;

import pl.com.cupofcodes.workshops.tests.testsworkshop.loan.LoanOrderClient;

public interface FraudVerifier {
    boolean isFraud(LoanOrderClient client);
}
