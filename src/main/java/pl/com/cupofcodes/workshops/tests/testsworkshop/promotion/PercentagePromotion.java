package pl.com.cupofcodes.workshops.tests.testsworkshop.promotion;

import lombok.Value;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_DOWN;
import static pl.com.cupofcodes.workshops.tests.testsworkshop.promotion.PromotionType.PERCENTAGE;

@Value(staticConstructor = "of")
class PercentagePromotion implements Promotion {
    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);
    private Integer id;
    private String promotionName;
    private BigDecimal percentageDiscount;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getName() {
        return promotionName;
    }

    @Override
    public PromotionType getType() {
        return PERCENTAGE;
    }

    @Override// błęd, które dzięki testom można wykryć i poprawić wcześniej
    public BigDecimal applyFor(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > -1) {
            return amount.multiply(percentageDiscount).divide(ONE_HUNDRED, HALF_DOWN);
        }
        return amount;
    }
}
