package pl.com.cupofcodes.workshops.tests.testsworkshop.promotion;

import java.math.BigDecimal;

public interface Promotion {
    Integer getId();

    String getName();

    PromotionType getType();

    BigDecimal applyFor(BigDecimal amount);
}
