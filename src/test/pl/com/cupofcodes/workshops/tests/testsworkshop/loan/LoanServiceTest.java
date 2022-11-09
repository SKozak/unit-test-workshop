package pl.com.cupofcodes.workshops.tests.testsworkshop.loan;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import pl.com.cupofcodes.workshops.tests.testsworkshop.EventEmitter;
import pl.com.cupofcodes.workshops.tests.testsworkshop.client.ClientService;
import pl.com.cupofcodes.workshops.tests.testsworkshop.froud.FraudVerifier;
import pl.com.cupofcodes.workshops.tests.testsworkshop.loan.esception.LoanAlreadyStartedCantModifyException;
import pl.com.cupofcodes.workshops.tests.testsworkshop.promotion.PromotionService;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoanServiceTest {
    @Mock
    LoanProperties loanProperties;
    @Mock
    FraudVerifier fraudVerifier;
    @Mock
    ClientService clientService;
    @Mock
    PromotionService promotionService;
    LoanRepository loanRepository = mock(LoanRepository.class);
    @Mock
    EventEmitter eventEmitter;

    LoanService loanService = new LoanService(loanProperties, fraudVerifier, clientService, promotionService, loanRepository, eventEmitter);

    @Test
    void canChangeInstallmentNotStartedLoan() {
        //given
        final UUID loanId = UUID.randomUUID();
        when(loanRepository.loadLoanBy(loanId)).thenReturn(aNotStartedLoan());

        //when
        final Loan loan = loanService.changeInstallments(5, loanId);

        //then
        assertThat(loan.getNumberOfInstallments()).isEqualTo(5);
    }

    @Test
    void shouldPreventOfChangeInstallmentsOnStartedLoan() {
        //given
        final UUID loanId = UUID.randomUUID();
        when(loanRepository.loadLoanBy(loanId)).thenReturn(aStartedLoan());

        //expected
        assertThatExceptionOfType(LoanAlreadyStartedCantModifyException.class)
                .isThrownBy(() -> loanService.changeInstallments(5, loanId));
    }


    private Loan aStartedLoan() {
        return new Loan(new BigDecimal(1000), 2, 22, true);
    }


    private Loan aNotStartedLoan() {
        return new Loan(new BigDecimal(1000), 2, 22, false);
    }

    @Test
    void test2() {
        final Loan loan1 = new Loan(new BigDecimal(1000), 2, 22, true);
        final LoanRepository loanRepository1 = new LoanRepository() {
            @Override
            public Loan createLoan(Loan loan) {
                return null;
            }

            @Override
            public Loan loadLoanBy(UUID loanId) {
                return loan1;
            }

            @Override
            public Loan updateLoan(Loan loan) {
                return null;
            }
        };
        LoanService loanService1 = new LoanService(null, null, null, null, loanRepository1, null);
        final UUID loanId = UUID.randomUUID();
        try {
            final Loan loan = loanService1.changeInstallments(5, loanId);
            assert loan.getNumberOfInstallments() == 5;
        } catch (Exception e) {
            if (e instanceof LoanAlreadyStartedCantModifyException) {
                assert true;
            } else {
                assert false;
            }
        }
    }


}
