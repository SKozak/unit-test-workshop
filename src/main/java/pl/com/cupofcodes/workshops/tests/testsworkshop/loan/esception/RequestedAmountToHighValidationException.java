package pl.com.cupofcodes.workshops.tests.testsworkshop.loan.esception;

import java.math.BigDecimal;

public class RequestedAmountToHighValidationException extends RuntimeException {
    public RequestedAmountToHighValidationException(BigDecimal calculatedPriceAfterFess, BigDecimal maxAmountOfAllowedMoney) {
        super(String.format("Price after calculation %.2f is too high. Client max allowed price is %.2f", calculatedPriceAfterFess, maxAmountOfAllowedMoney));
    }
}
