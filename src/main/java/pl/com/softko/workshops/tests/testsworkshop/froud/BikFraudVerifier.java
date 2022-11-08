package pl.com.softko.workshops.tests.testsworkshop.froud;

import org.springframework.stereotype.Service;
import pl.com.softko.workshops.tests.testsworkshop.loan.LoanOrderClient;

@Service
class BikFraudVerifier implements FraudVerifier {
    @Override
    public boolean isFraud(LoanOrderClient client) {
        return false;
    }
}
