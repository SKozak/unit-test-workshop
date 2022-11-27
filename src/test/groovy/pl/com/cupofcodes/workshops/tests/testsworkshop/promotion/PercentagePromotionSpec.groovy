package pl.com.cupofcodes.workshops.tests.testsworkshop.promotion


import spock.lang.Specification

import static org.assertj.core.api.Assertions.assertThat
import static pl.com.cupofcodes.workshops.tests.testsworkshop.AmountFixture._1000
import static pl.com.cupofcodes.workshops.tests.testsworkshop.AmountFixture._1200
import static pl.com.cupofcodes.workshops.tests.testsworkshop.AmountFixture._850
import static pl.com.cupofcodes.workshops.tests.testsworkshop.promotion.PromotionFixture.aPercentagePromotion

class PercentagePromotionSpec extends Specification {


    def "should apply promotion when end price is grater than zero"() {
        given:
            final PercentagePromotion promotion = aPercentagePromotion("0.15")
        when:
            BigDecimal result = promotion.applyFor(_1000)
        then:
            assertThat(result).isEqualTo(_850)
    }

    def "should return price before promotion when end price is lover than zero"() {
        given:
            final PercentagePromotion promotion = aPercentagePromotion("1.15")
        when:
            BigDecimal result = promotion.applyFor(1200 as BigDecimal)
        then:
            assertThat(result).isEqualTo(_1200)
    }

    def "should return price before promotion when end price is zero"() {
        given:
            final PercentagePromotion promotion = aPercentagePromotion("1.00")
        when:
            BigDecimal result = promotion.applyFor(_1000)
        then:
            assertThat(result).isEqualTo(_1000)
    }

    def "should return expected for promotion of #promotionPercent % and initialPrice of #initialPrice"(BigDecimal expected, String promotionPercent, BigDecimal initialPrice) {
        given:
            final PercentagePromotion promotion = aPercentagePromotion(promotionPercent)
        when:
            BigDecimal result = promotion.applyFor(initialPrice)
        then:
            assertThat(result).isEqualTo(expected)
        where:
            initialPrice | promotionPercent || expected
            _1000        | "0.15"           || _850
            _1200        | "1.15"           || _1200
            _1000        | "1.00"           || _1000
    }
}
