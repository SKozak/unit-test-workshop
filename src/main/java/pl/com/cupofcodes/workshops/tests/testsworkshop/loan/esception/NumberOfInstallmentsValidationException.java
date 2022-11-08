package pl.com.cupofcodes.workshops.tests.testsworkshop.loan.esception;

public class NumberOfInstallmentsValidationException extends RuntimeException {
    public NumberOfInstallmentsValidationException(int requestedInstallments, int maxAmountOfInstallments) {
        super(String.format("Client requested for %d installments but max allowed for that client is %d", requestedInstallments, maxAmountOfInstallments));
    }
}
