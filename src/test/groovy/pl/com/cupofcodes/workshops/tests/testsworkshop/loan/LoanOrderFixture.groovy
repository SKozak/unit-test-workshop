package pl.com.cupofcodes.workshops.tests.testsworkshop.loan

import java.time.LocalDate

class LoanOrderFixture {
    static final Map SAMPLE_LOAN = [
            "amount" :  100,
            "installmentsAmount" :  2,
            "grantedPromotionsIds" : [2,3],
            "loanOrderClient" : new LoanOrderClient(1,"test","test2","1241526266","1IDsada", LocalDate.now())
    ]


    static LoanOrder loanOrderWith(Map<String,Object> properties = [:]){
        properties = SAMPLE_LOAN + properties

        return new LoanOrder(
                properties.amount as BigDecimal,
                properties.installmentsAmount as Integer,
                properties.grantedPromotionsIds as List<Integer>,
                properties.loanOrderClient as LoanOrderClient
        )
    }
}
