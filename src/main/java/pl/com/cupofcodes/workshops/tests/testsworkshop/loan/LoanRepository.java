package pl.com.cupofcodes.workshops.tests.testsworkshop.loan;

import java.util.UUID;

interface LoanRepository {
    Loan createLoan(Loan loan);

    Loan loadLoanBy(UUID loanId);

    Loan updateLoan(Loan loan);
}
