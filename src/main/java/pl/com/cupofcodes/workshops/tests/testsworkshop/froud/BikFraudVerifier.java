package pl.com.cupofcodes.workshops.tests.testsworkshop.froud;

import org.springframework.stereotype.Service;
import pl.com.cupofcodes.workshops.tests.testsworkshop.loan.LoanOrderClient;

@Service
class BikFraudVerifier implements FraudVerifier {
    @Override
    public boolean isFraud(LoanOrderClient client) {
        return false;
    }
}
