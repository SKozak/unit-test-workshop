package pl.com.cupofcodes.workshops.tests.testsworkshop.loan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import pl.com.cupofcodes.workshops.tests.testsworkshop.EventEmitter;
import pl.com.cupofcodes.workshops.tests.testsworkshop.client.ClientService;
import pl.com.cupofcodes.workshops.tests.testsworkshop.client.ClientType;
import pl.com.cupofcodes.workshops.tests.testsworkshop.froud.FraudVerifier;
import pl.com.cupofcodes.workshops.tests.testsworkshop.loan.esception.FraudDetectedException;
import pl.com.cupofcodes.workshops.tests.testsworkshop.loan.esception.LoanAlreadyStartedCantModifyException;
import pl.com.cupofcodes.workshops.tests.testsworkshop.loan.esception.NumberOfInstallmentsValidationException;
import pl.com.cupofcodes.workshops.tests.testsworkshop.loan.esception.RequestedAmountToHighValidationException;
import pl.com.cupofcodes.workshops.tests.testsworkshop.promotion.PromotionService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.com.cupofcodes.workshops.tests.testsworkshop.AmountFixture.amountOf;
import static pl.com.cupofcodes.workshops.tests.testsworkshop.client.ClientType.NORMAL;
import static pl.com.cupofcodes.workshops.tests.testsworkshop.client.ClientType.REGULAR;
import static pl.com.cupofcodes.workshops.tests.testsworkshop.client.ClientType.STUDENT;
import static pl.com.cupofcodes.workshops.tests.testsworkshop.client.ClientType.VIP;
import static pl.com.cupofcodes.workshops.tests.testsworkshop.loan.LoanAssertions.assertThats;
import static pl.com.cupofcodes.workshops.tests.testsworkshop.loan.LoanFixture.aLoanOrder;
import static pl.com.cupofcodes.workshops.tests.testsworkshop.loan.LoanFixture.aNotStartedLoan;
import static pl.com.cupofcodes.workshops.tests.testsworkshop.loan.LoanFixture.aStartedLoan;

class LoanServiceTest {
    private static final List<Integer> NO_PROMOTIONS = List.of();
    private static final BigDecimal _1000 = amountOf(1000);
    LoanProperties loanProperties = mock(LoanProperties.class);
    FraudVerifier fraudVerifier = mock(FraudVerifier.class);
    ClientService clientService = mock(ClientService.class);
    PromotionService promotionService = mock(PromotionService.class);
    LoanRepository loanRepository = mock(LoanRepository.class);
    EventEmitter eventEmitter = mock(EventEmitter.class);
    LoanConstraintValidator loanConstraintValidator = new LoanConstraintValidator(loanProperties, clientService);

    LoanService loanService = new LoanService(fraudVerifier, promotionService, loanRepository, eventEmitter, loanConstraintValidator);

    @BeforeEach
    void beforeEach() {
        when(loanProperties.getMaxAmountOfInstallments()).thenReturn(6);
        when(loanProperties.getMaxAmountOfMoney()).thenReturn(amountOf(1500));
    }

    @Test
    void canChangeInstallmentOnNotStartedLoan() {
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

    @Test
    void should_throw_exception_when_fraud_detected() {
        //given
        when(fraudVerifier.isFraud(any())).thenReturn(true);

        //expected
        assertThatExceptionOfType(FraudDetectedException.class)
                .isThrownBy(() -> loanService.createLoan(aLoanOrder(NO_PROMOTIONS, _1000)));
    }

    @Test
    void can_create_loan() {
        //given
        when(promotionService.calculatePriceAfterPromotions(any(), any())).thenReturn(_1000);
        when(clientService.getClientType(anyLong())).thenReturn(NORMAL);
        LoanOrder loanOrder = aLoanOrder(NO_PROMOTIONS, _1000);

        //when
        final Loan loan = loanService.createLoan(loanOrder);

        //then
        assertThat(loan).isNotNull();
    }

    @Test
    void can_create_loan_assert_object_example() {
        //given
        when(promotionService.calculatePriceAfterPromotions(any(), any())).thenReturn(_1000);
        when(clientService.getClientType(anyLong())).thenReturn(NORMAL);
        LoanOrder loanOrder = aLoanOrder(NO_PROMOTIONS, _1000);

        //when
        final Loan loan = loanService.createLoan(loanOrder);

        //then
        assertThats(loan)
                .hasNumberOfInstallmentsEqual(4)
                .amountIsEqual(1200);
    }

    @Test
    void should_emit_event_with_correct_loan_id_when_loan_granted() {
        //given
        when(promotionService.calculatePriceAfterPromotions(any(), any())).thenReturn(_1000);
        when(clientService.getClientType(anyLong())).thenReturn(NORMAL);
        LoanOrder loanOrder = aLoanOrder(NO_PROMOTIONS, _1000);
        ArgumentCaptor<LoanGrantedEvent> loanGrantedEventArgumentCaptor = ArgumentCaptor
                .forClass(LoanGrantedEvent.class);

        //when
        final Loan loan = loanService.createLoan(loanOrder);

        //then
        verify(eventEmitter).emit(loanGrantedEventArgumentCaptor.capture());
        assertThat(loanGrantedEventArgumentCaptor.getValue().getLoanId()).isEqualTo(loan.getId());
    }

    @Test
    void should_save_loan_if_granted() {
        //given
        when(promotionService.calculatePriceAfterPromotions(any(), any())).thenReturn(_1000);
        when(clientService.getClientType(anyLong())).thenReturn(NORMAL);
        LoanOrder loanOrder = aLoanOrder(NO_PROMOTIONS, _1000);

        //when
        final Loan loan = loanService.createLoan(loanOrder);

        //then
        verify(loanRepository, only()).createLoan(loan);
    }

    @ParameterizedTest(name = "should return NumberOfInstallmentsValidationException for clientType {0} when amount is {1}, and installment size is {2}")
    //unrolling
    @MethodSource("loanOrderArgumentProvider")
    void shouldRiseAnExceptionWhenMaxInstallmentsConstraintsForClientTypeExceeded(ClientType clientType,
                                                                                  BigDecimal requestedAmount, int numberOfInstallments) {
        //given
        when(promotionService.calculatePriceAfterPromotions(any(), any())).thenReturn(requestedAmount);
        when(clientService.getClientType(anyLong())).thenReturn(clientType);
        LoanOrder loanOrder = aLoanOrder(NO_PROMOTIONS, requestedAmount, numberOfInstallments);

        //expected
        assertThatExceptionOfType(NumberOfInstallmentsValidationException.class)
                .isThrownBy(() -> loanService.createLoan(loanOrder));
    }

    static Stream<Arguments> loanOrderArgumentProvider() {
        return Stream.of(
                arguments(NORMAL, _1000, 7), //magicNumbers refactor
                arguments(VIP, _1000, 13),
                arguments(REGULAR, _1000, 9),
                arguments(STUDENT, _1000, 11)
        );
    }

    @ParameterizedTest(name = "should return RequestedAmountToHighValidationException for clientType {0} when amount is {1}, and installment size is {2}")
    //unrolling
    @MethodSource("loanOrderArgumentProvider2")
    void shouldRiseAnExceptionWhenMaxAmountConstraintsForClientTypeExceeded(ClientType clientType,
                                                                            BigDecimal requestedAmount, int numberOfInstallments) {
        //given
        when(promotionService.calculatePriceAfterPromotions(any(), any())).thenReturn(requestedAmount);
        when(clientService.getClientType(anyLong())).thenReturn(clientType);
        LoanOrder loanOrder = aLoanOrder(NO_PROMOTIONS, requestedAmount, numberOfInstallments);

        //expected
        assertThatExceptionOfType(RequestedAmountToHighValidationException.class)
                .isThrownBy(() -> loanService.createLoan(loanOrder));
    }


    static Stream<Arguments> loanOrderArgumentProvider2() { //odnieść się do spocka
        return Stream.of(
                arguments(NORMAL, amountOf(3000), 5), //magicNumbers refactor
                arguments(VIP, amountOf(2500), 5),
                arguments(REGULAR, amountOf(4500), 5),
                arguments(STUDENT, amountOf(5000), 5)
        );
    }
}
