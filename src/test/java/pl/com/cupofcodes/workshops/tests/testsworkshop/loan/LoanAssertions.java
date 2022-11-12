package pl.com.cupofcodes.workshops.tests.testsworkshop.loan;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class LoanAssertions {
    private Loan loan;

    public LoanAssertions(Loan loan) {
        this.loan = loan;
    }

    public static LoanAssertions assertThats(Loan loan) {
        return new LoanAssertions(loan);
    }

    public LoanAssertions hasNumberOfInstallmentsEqual(int expected) {
        assertThat(loan.getNumberOfInstallments()).isEqualTo(expected);
        return this;
    }

    public LoanAssertions amountIsEqual(int expected) {
        assertThat(loan.getCalculatedPriceAfterFess()).isEqualTo(new BigDecimal(expected));
        return this;
    }
}
