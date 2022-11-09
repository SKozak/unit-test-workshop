package pl.com.cupofcodes.workshops.tests.testsworkshop.loan;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
class Loan {
    private final UUID id = UUID.randomUUID();
    private final BigDecimal calculatedPriceAfterFess;
    private Integer numberOfInstallments;
    private final long clientId;
    private final boolean loanStarted;

    Loan(BigDecimal calculatedPriceAfterFess, Integer numberOfInstallments, long clientId, boolean loanStarted) {
        this.calculatedPriceAfterFess = calculatedPriceAfterFess;
        this.numberOfInstallments = numberOfInstallments;
        this.clientId = clientId;
        this.loanStarted = loanStarted;
    }

    Loan(BigDecimal calculatedPriceAfterFess, Integer numberOfInstallments, long clientId) {
        this.calculatedPriceAfterFess = calculatedPriceAfterFess;
        this.numberOfInstallments = numberOfInstallments;
        this.clientId = clientId;
        this.loanStarted = true;
    }

    public void changeInstallments(int newInstallmentsNumber) {
        this.numberOfInstallments = newInstallmentsNumber;
    }
}
