package pl.com.cupofcodes.workshops.tests.testsworkshop.loan;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static pl.com.cupofcodes.workshops.tests.testsworkshop.AmountFixture.amountOf;

class LoanFixture {

    public static Loan aStartedLoan() {
        return new Loan(amountOf(1200), 2, 22, true);
    }


    public static Loan aNotStartedLoan() {
        return new Loan(amountOf(1200), 2, 22, false);
    }

    public static LoanOrder aLoanOrder(List<Integer> promotionsIds, BigDecimal amount) {
        return aLoanOrder(promotionsIds, amount, 4);
    }

    public static LoanOrder aLoanOrder(List<Integer> promotionsIds, BigDecimal amount, int numberOfInstallments) {
        return new LoanOrder(amount, numberOfInstallments, promotionsIds, aClient());
    }

    public static LoanOrderClient aClient() {
        return new LoanOrderClient(1, "name", "surname", "123412125", "13123", LocalDate.of(1994, 4, 12));
    }
}
