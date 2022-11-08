package pl.com.softko.workshops.tests.testsworkshop.loan;

import java.math.BigDecimal;
import java.util.List;

public record LoanOrder(BigDecimal amount, int installmentsAmount, List<Integer> grantedPromotionsIds,
                        LoanOrderClient loanOrderClient) {
}
