package pl.com.cupofcodes.workshops.tests.testsworkshop.loan;

import lombok.RequiredArgsConstructor;
import pl.com.cupofcodes.workshops.tests.testsworkshop.client.ClientService;
import pl.com.cupofcodes.workshops.tests.testsworkshop.client.ClientType;
import pl.com.cupofcodes.workshops.tests.testsworkshop.loan.esception.NumberOfInstallmentsValidationException;
import pl.com.cupofcodes.workshops.tests.testsworkshop.loan.esception.RequestedAmountToHighValidationException;

import java.math.BigDecimal;

@RequiredArgsConstructor
class LoanConstraintValidator {
    private final LoanProperties loanProperties;
    private final ClientService clientService;

    public void validateConstraints(long clientId, BigDecimal priceAfterFess, int installmentsAmount) {
        final ClientType clientType = clientService.getClientType(clientId);


        switch (clientType) {
            case VIP:
                validateLoanConstraintsNotExceeded(priceAfterFess, installmentsAmount, 12, 0.25);
                break;
            case REGULAR:
                validateLoanConstraintsNotExceeded(priceAfterFess, installmentsAmount, 8, 0.15);
                break;
            case STUDENT:
                validateLoanConstraintsNotExceeded(priceAfterFess, installmentsAmount, 10, 0.10);
                break;
            case NORMAL:
                validateLoanConstraintsNotExceeded(priceAfterFess, installmentsAmount);
                break;
        }
    }

    private void validateLoanConstraintsNotExceeded(BigDecimal calculatedPriceAfterFess, int requestedInstallments) {
        final BigDecimal maxAmountOfAllowedMoney = loanProperties.getMaxAmountOfMoney();
        final int maxAmountOfInstallments = loanProperties.getMaxAmountOfInstallments();

        if (requestedInstallments > maxAmountOfInstallments) {
            throw new NumberOfInstallmentsValidationException(requestedInstallments, maxAmountOfInstallments);
        }

        if (calculatedPriceAfterFess.compareTo(maxAmountOfAllowedMoney) > 0) {
            throw new RequestedAmountToHighValidationException(calculatedPriceAfterFess, maxAmountOfAllowedMoney);
        }
    }

    private void validateLoanConstraintsNotExceeded(BigDecimal calculatedPriceAfterFess, int requestedInstallments, int maxInstallments, double allowedExceeding) {
        final BigDecimal maxAmountOfAllowedMoney = loanProperties.getMaxAmountOfMoney();

        if (requestedInstallments > maxInstallments) {
            throw new NumberOfInstallmentsValidationException(requestedInstallments, maxInstallments);
        }

        final BigDecimal maxAllowedWithExceeding = maxAmountOfAllowedMoney.multiply(BigDecimal.valueOf(allowedExceeding));
        if (maxAllowedWithExceeding.compareTo(calculatedPriceAfterFess) < 0) {
            throw new RequestedAmountToHighValidationException(calculatedPriceAfterFess, maxAllowedWithExceeding);
        }
    }

}
