package pl.com.cupofcodes.workshops.tests.testsworkshop;

import java.math.BigDecimal;

public class AmountFixture {
    public static final BigDecimal _200 = amountOf(200);
    public static final BigDecimal _800 = amountOf(800);
    public static final BigDecimal _1000 = amountOf(1_000);
    public static final BigDecimal _500 = amountOf(500);
    public static final BigDecimal _1200 = new BigDecimal(1200);
    public static final BigDecimal _850 = new BigDecimal("850.00");


    public static BigDecimal amountOf(int val) {
        return new BigDecimal(val);
    }
}
