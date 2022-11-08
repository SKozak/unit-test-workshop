package pl.com.cupofcodes.workshops.tests.testsworkshop.loan;

import java.math.BigDecimal;
import java.util.List;

public record LoanOrder(BigDecimal amount, int installmentsAmount, List<Integer> grantedPromotionsIds,
                        LoanOrderClient loanOrderClient) {
}
