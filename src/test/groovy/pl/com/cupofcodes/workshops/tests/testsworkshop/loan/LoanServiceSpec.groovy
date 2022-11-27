package pl.com.cupofcodes.workshops.tests.testsworkshop.loan

import pl.com.cupofcodes.workshops.tests.testsworkshop.EventEmitter
import pl.com.cupofcodes.workshops.tests.testsworkshop.client.ClientService
import pl.com.cupofcodes.workshops.tests.testsworkshop.froud.FraudVerifier
import pl.com.cupofcodes.workshops.tests.testsworkshop.loan.esception.FraudDetectedException
import pl.com.cupofcodes.workshops.tests.testsworkshop.loan.esception.LoanAlreadyStartedCantModifyException
import pl.com.cupofcodes.workshops.tests.testsworkshop.promotion.PromotionService
import spock.lang.Specification

import static pl.com.cupofcodes.workshops.tests.testsworkshop.AmountFixture.amountOf
import static pl.com.cupofcodes.workshops.tests.testsworkshop.client.ClientType.NORMAL
import static pl.com.cupofcodes.workshops.tests.testsworkshop.loan.LoanAssertions.assertThats
import static pl.com.cupofcodes.workshops.tests.testsworkshop.loan.LoanFixture.aLoanOrder
import static pl.com.cupofcodes.workshops.tests.testsworkshop.loan.LoanFixture.aNotStartedLoan
import static pl.com.cupofcodes.workshops.tests.testsworkshop.loan.LoanFixture.aStartedLoan
import static pl.com.cupofcodes.workshops.tests.testsworkshop.loan.LoanOrderFixture.loanOrderWith

class LoanServiceSpec extends Specification {
    private static final List<Integer> NO_PROMOTIONS = []
    private static final BigDecimal _1000 = amountOf(1000)
    def loanProperties = Mock(LoanProperties)
    def fraudVerifier = Mock(FraudVerifier)
    def clientService = Mock(ClientService)
    def promotionService = Mock(PromotionService)
    def loanRepository = Mock(LoanRepository)
    def eventEmitter = Mock(EventEmitter)
    def loanValidator = new LoanConstraintValidator(loanProperties, clientService)

    LoanService loanService = new LoanService(fraudVerifier, promotionService, loanRepository, eventEmitter, loanValidator)

    def setup() {
        loanProperties.getMaxAmountOfInstallments() >> 6
        loanProperties.getMaxAmountOfMoney() >> amountOf(1500)
    }

    void "can change installment on not started loan"() {
        given:
            final UUID loanId = UUID.randomUUID()
            loanRepository.loadLoanBy(loanId) >> aNotStartedLoan()
        when:
            final Loan loan = loanService.changeInstallments(5, loanId)
        then:
            loan.getNumberOfInstallments() == 5
    }

    def "should prevent of change installments on started loan"() {
        given:
            final UUID loanId = UUID.randomUUID()
            loanRepository.loadLoanBy(loanId) >> aStartedLoan()
        when:
            loanService.changeInstallments(5, loanId)
        then:
            thrown(LoanAlreadyStartedCantModifyException)
    }


    def "should throw exception when fraud detected"() {
        given:
            fraudVerifier.isFraud(_ as LoanOrderClient) >> true

        when:
            loanService.createLoan(aLoanOrder(NO_PROMOTIONS, _1000))
        then:
            thrown(FraudDetectedException)
    }

    def "can create loan"() {
        given:
            promotionService.calculatePriceAfterPromotions(_ as List<Integer>, _ as BigDecimal) >> _1000
            clientService.getClientType(_ as Long) >> NORMAL
            LoanOrder loanOrder = loanOrderWith(amount: _1000, grantedPromotionsIds: [])
        when:
            final Loan loan = loanService.createLoan(loanOrder)
        then:
            loan != null
    }

    void "can create loan assert object example"() {
        given:
            promotionService.calculatePriceAfterPromotions(_ as List<Integer>, _ as BigDecimal) >> _1000
            clientService.getClientType(_ as Long) >> NORMAL
            LoanOrder loanOrder = aLoanOrder(NO_PROMOTIONS, _1000)

        when:
            final Loan loan = loanService.createLoan(loanOrder)

        then:
            assertThats(loan)
                    .hasNumberOfInstallmentsEqual(4)
                    .amountIsEqual(1200)
    }


    def "should emit event with correct loan id when loan granted"() {
        given:
            LoanGrantedEvent capturedEvent
            promotionService.calculatePriceAfterPromotions(_ as List<Integer>, _ as BigDecimal) >> _1000
            clientService.getClientType(_ as Long) >> NORMAL
            LoanOrder loanOrder = aLoanOrder(NO_PROMOTIONS, _1000)
        when:
            Loan loan = loanService.createLoan(loanOrder)

        then:
            1 * eventEmitter.emit(_) >> { LoanGrantedEvent event ->
                capturedEvent = event
            }
        and:
            capturedEvent.getLoanId() == loan.getId()

    }

    def "should_save_loan_if_granted"() {
        given:
            promotionService.calculatePriceAfterPromotions(_ as List<Integer>, _ as BigDecimal) >> _1000
            clientService.getClientType(_ as Long) >> NORMAL
            LoanOrder loanOrder = aLoanOrder(NO_PROMOTIONS, _1000)
        when:
            loanService.createLoan(loanOrder)
        then:
            1 * loanRepository.createLoan(_)
    }
}
