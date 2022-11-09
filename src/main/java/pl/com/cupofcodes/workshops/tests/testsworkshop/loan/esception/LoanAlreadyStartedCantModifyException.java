package pl.com.cupofcodes.workshops.tests.testsworkshop.loan.esception;

public class LoanAlreadyStartedCantModifyException extends RuntimeException {
    public LoanAlreadyStartedCantModifyException(String message) {
        super(message);
    }
}
