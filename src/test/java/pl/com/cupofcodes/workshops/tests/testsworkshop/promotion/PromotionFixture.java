package pl.com.cupofcodes.workshops.tests.testsworkshop.promotion;

import java.math.BigDecimal;

import static pl.com.cupofcodes.workshops.tests.testsworkshop.AmountFixture.amountOf;

class PromotionFixture {
    public static AmountPromotion aAmountPromotion(int val) {
        return AmountPromotion.of(1, "Student Promo", amountOf(val));
    }

    public static PercentagePromotion aPercentagePromotion(String percentageDiscount) {
        return PercentagePromotion.of(1, "Student promo", new BigDecimal(percentageDiscount));
    }
}
