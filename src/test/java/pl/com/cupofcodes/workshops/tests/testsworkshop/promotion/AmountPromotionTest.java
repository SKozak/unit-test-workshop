package pl.com.cupofcodes.workshops.tests.testsworkshop.promotion;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static pl.com.cupofcodes.workshops.tests.testsworkshop.AmountFixture.amountOf;

class AmountPromotionTest {

    private static final BigDecimal _200 = amountOf(200);
    private static final BigDecimal _800 = amountOf(800);
    private static final BigDecimal _1000 = amountOf(1_000);
    private static final BigDecimal _500 = amountOf(500);

    @Test
    void should_apply_promotion_when_end_price_is_grater_than_zero() {
        //given
        final AmountPromotion promotion = aPromotion(300);

        //when
        BigDecimal result = promotion.applyFor(_500);

        //then
        assertThat(result).isEqualTo(_200);
    }

    @Test
    void should_return_price_before_promotion_when_end_price_is_lover_than_zero() {
        //given
        final AmountPromotion promotion = aPromotion(1_000);

        //when
        BigDecimal result = promotion.applyFor(_800);

        //then
        assertThat(result).isEqualTo(_800);
    }

    @Test
    void should_return_price_before_promotion_when_end_price_is_zero() {
        //given
        final AmountPromotion promotion = aPromotion(1_000);

        //when
        BigDecimal result = promotion.applyFor(_1000);

        //then
        assertThat(result).isEqualTo(_1000);
    }


    //LUB
    @ParameterizedTest(name = "should return {0} for promotion of {1} and initialPrice of {2}") //unrolling
    @MethodSource("promotionArgumentsProvider")
    void shouldApplyPromotionWhenEndPriceIsGraterThanZero(BigDecimal expectedResult, BigDecimal promotion,
                                                          BigDecimal initialPrice) {
        //given
        final AmountPromotion aPromotion = aPromotion(promotion);

        //when
        BigDecimal result = aPromotion.applyFor(initialPrice);

        //then
        assertThat(result).isEqualTo(expectedResult);
    }

    static Stream<Arguments> promotionArgumentsProvider() {
        return Stream.of(
                arguments(_800, _1000, _800),
                arguments(_200, amountOf(300), _500)
                //                arguments(),
                //                arguments()
        );
    }


    private static AmountPromotion aPromotion(int val) {
        return AmountPromotion.of(1, "Student Promo", amountOf(val));
    }

    private static AmountPromotion aPromotion(BigDecimal value) {
        return AmountPromotion.of(1, "Student Promo", value);
    }

}
