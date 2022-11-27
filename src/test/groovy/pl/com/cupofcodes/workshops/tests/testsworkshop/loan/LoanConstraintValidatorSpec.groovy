package pl.com.cupofcodes.workshops.tests.testsworkshop.loan

import pl.com.cupofcodes.workshops.tests.testsworkshop.client.ClientService
import pl.com.cupofcodes.workshops.tests.testsworkshop.client.ClientType
import pl.com.cupofcodes.workshops.tests.testsworkshop.loan.esception.NumberOfInstallmentsValidationException
import pl.com.cupofcodes.workshops.tests.testsworkshop.loan.esception.RequestedAmountToHighValidationException
import spock.lang.Specification
import spock.lang.Unroll

import static pl.com.cupofcodes.workshops.tests.testsworkshop.AmountFixture._1000
import static pl.com.cupofcodes.workshops.tests.testsworkshop.AmountFixture.amountOf
import static pl.com.cupofcodes.workshops.tests.testsworkshop.client.ClientType.NORMAL
import static pl.com.cupofcodes.workshops.tests.testsworkshop.client.ClientType.REGULAR
import static pl.com.cupofcodes.workshops.tests.testsworkshop.client.ClientType.STUDENT
import static pl.com.cupofcodes.workshops.tests.testsworkshop.client.ClientType.VIP

class LoanConstraintValidatorSpec extends Specification {
    def clientService = Stub(ClientService)
    def loanProperties = Mock(LoanProperties)
    def loanValidator = new LoanConstraintValidator(loanProperties, clientService)

    def setup() {
        loanProperties.getMaxAmountOfInstallments() >> 6
        loanProperties.getMaxAmountOfMoney() >> amountOf(1500)
    }


    //we can use @Unroll if we want different specific test name than grouping one
    @Unroll("should return NumberOfInstallmentsValidationException for clientType  #clientType  when amount is #requestedAmount, and installment size is #numberOfInstallments")
    def "should rise an exception when max installments constraints for client type exceeded"(ClientType clientType,
                                                                                              BigDecimal requestedAmount,
                                                                                              int numberOfInstallments) {
        given:
            clientService.getClientType(_ as Long) >> clientType
        when:
            loanValidator.validateConstraints(1, requestedAmount, numberOfInstallments)
        then:
            thrown(NumberOfInstallmentsValidationException)
        where:
            clientType | requestedAmount | numberOfInstallments
            NORMAL     | _1000           | 7
            VIP        | _1000           | 13
            REGULAR    | _1000           | 9
            STUDENT    | _1000           | 11
    }

    def "should return RequestedAmountToHighValidationException for clientType #clientType when amount is #requestedAmount, and installment size is #numberOfInstallments"(ClientType clientType,
                                                                                                                                                                           BigDecimal requestedAmount,
                                                                                                                                                                           int numberOfInstallments) {
        given:
            clientService.getClientType(_ as Long) >> clientType
        when:
            loanValidator.validateConstraints(1, requestedAmount, numberOfInstallments)

        then:
            thrown(RequestedAmountToHighValidationException)

        where:
            clientType | requestedAmount | numberOfInstallments
            NORMAL     | amountOf(3000)  | 5
            VIP        | amountOf(2500)  | 5
            REGULAR    | amountOf(4500)  | 5
            STUDENT    | amountOf(5000)  | 5
    }

}
