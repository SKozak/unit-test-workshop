package pl.com.cupofcodes.workshops.tests.testsworkshop.promotion;

import lombok.Value;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;
import static pl.com.cupofcodes.workshops.tests.testsworkshop.promotion.PromotionType.AMOUNT;

@Value(staticConstructor = "of")
class AmountPromotion implements Promotion {
    private final Integer id;
    private final String promotionName;
    private final BigDecimal amountToSubtract;

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
        return AMOUNT;
    }

    @Override
    public BigDecimal applyFor(BigDecimal amount) {
        final BigDecimal subtracted = amount.subtract(amountToSubtract);
        if (subtracted.compareTo(ZERO) > 0) {
            return subtracted;
        }
        return amount;
    }
}
