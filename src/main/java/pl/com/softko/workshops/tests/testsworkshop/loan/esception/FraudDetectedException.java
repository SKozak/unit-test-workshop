package pl.com.softko.workshops.tests.testsworkshop.loan.esception;

import pl.com.softko.workshops.tests.testsworkshop.loan.LoanOrder;

public class FraudDetectedException extends RuntimeException {

    public FraudDetectedException(LoanOrder loanOrder) {
        super("Fraud detected for loan order" + loanOrder.toString());
    }
}
