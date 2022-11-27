package pl.com.cupofcodes.workshops.tests.testsworkshop.promotion


import spock.lang.Specification

import static org.assertj.core.api.Assertions.assertThat
import static pl.com.cupofcodes.workshops.tests.testsworkshop.AmountFixture._1000
import static pl.com.cupofcodes.workshops.tests.testsworkshop.AmountFixture._200
import static pl.com.cupofcodes.workshops.tests.testsworkshop.AmountFixture._500
import static pl.com.cupofcodes.workshops.tests.testsworkshop.AmountFixture._800
import static pl.com.cupofcodes.workshops.tests.testsworkshop.promotion.PromotionFixture.aAmountPromotion

class AmountPromotionSpec extends Specification {

    def "should apply promotion when end price is grater than zero"() {
        given:
            final AmountPromotion promotion = aAmountPromotion(300)

        when:
            BigDecimal result = promotion.applyFor(_500)

        then:
            result == _200
    }

    def "should return price before promotion when end price is lover than zero"() {
        given:
            final AmountPromotion promotion = aAmountPromotion(1_000);

        when:
            BigDecimal result = promotion.applyFor(_800);

        then:
            assertThat(result).isEqualTo(_800);
    }

    def "should return price before promotion when end price is zero"() {
        given:
            final AmountPromotion promotion = aAmountPromotion(1_000);

        when:
            BigDecimal result = promotion.applyFor(_1000);

        then:
            assertThat(result).isEqualTo(_1000);
    }


    void "should return #expected for promotion of #promotion and initialPrice of #initialPrice"(BigDecimal expected,
                                                                                                 Integer promotion,
                                                                                                 BigDecimal initialPrice) {
        given:
            final AmountPromotion aPromotion = aAmountPromotion(promotion);

        when:
            BigDecimal result = aPromotion.applyFor(initialPrice);

        then:
            assertThat(result).isEqualTo(expected);

        where:
            promotion | initialPrice || expected
            1000      | 800          || 800
            300       | 500          || 200
            900       | 200          || 200

    }

}
