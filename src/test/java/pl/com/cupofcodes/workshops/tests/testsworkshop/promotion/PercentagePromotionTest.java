package pl.com.cupofcodes.workshops.tests.testsworkshop.promotion;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static pl.com.cupofcodes.workshops.tests.testsworkshop.AmountFixture._1000;
import static pl.com.cupofcodes.workshops.tests.testsworkshop.AmountFixture._1200;
import static pl.com.cupofcodes.workshops.tests.testsworkshop.AmountFixture._850;

class PercentagePromotionTest {

    @Test
    void should_apply_promotion_when_end_price_is_grater_than_zero() {
        //given
        final PercentagePromotion promotion = aPromotion("0.15");

        //when
        BigDecimal result = promotion.applyFor(_1000);

        //then
        assertThat(result).isEqualTo(_850);
    }

    @Test
    void should_return_price_before_promotion_when_end_price_is_lover_than_zero() {
        //given
        final PercentagePromotion promotion = aPromotion("1.15");

        //when
        BigDecimal result = promotion.applyFor(_1200);

        //then
        assertThat(result).isEqualTo(_1200);
    }

    @Test
    void should_return_price_before_promotion_when_end_price_is_zero() {
        //given
        final PercentagePromotion promotion = aPromotion("1.00");

        //when
        BigDecimal result = promotion.applyFor(_1000);

        //then
        assertThat(result).isEqualTo(_1000);
    }

    //LUB
    @ParameterizedTest(name = "should return {0} for promotion of {1}% and initialPrice of {2}") //unrolling
    @MethodSource("promotionArgumentsProvider")
    void shouldApplyPromotionWhenEndPriceIsGraterThanZero(BigDecimal expectedResult, String promotionPercent,
                                                          BigDecimal initialPrice) {
        //given
        final PercentagePromotion promotion = aPromotion(promotionPercent);

        //when
        BigDecimal result = promotion.applyFor(initialPrice);

        //then
        assertThat(result).isEqualTo(expectedResult);
    }

    static Stream<Arguments> promotionArgumentsProvider() {
        return Stream.of(
                arguments(_850, "0.15", _1000),
                arguments(_1200, "1.15", _1200),
                arguments(_1000, "1.00", _1000)
        );
    }


    private PercentagePromotion aPromotion(String percentageDiscount) {
        return PercentagePromotion.of(1, "Student promo", new BigDecimal(percentageDiscount));
    }
}
