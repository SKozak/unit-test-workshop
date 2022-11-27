package pl.com.cupofcodes.workshops.tests.testsworkshop.promotion;

import lombok.Value;

import java.math.BigDecimal;

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

    @Override
    public BigDecimal applyFor(BigDecimal amount) {
        final BigDecimal resultAmount = amount.subtract(amount.multiply(percentageDiscount));
        if (resultAmount.compareTo(BigDecimal.ZERO) > 0 ) {
            return resultAmount;
        }
        return amount;
    }
}
