package pl.com.cupofcodes.workshops.tests.testsworkshop.promotion;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
class PromotionServiceTest {
    private static final BigDecimal _1000 = new BigDecimal(1000);
    PromotionRepository promotionRepository = mock(PromotionRepository.class);
    PromotionService promotionService = new PromotionService(promotionRepository);

    @BeforeAll
    static void beforeAllTests() {
        log.info("Running all tests.");
    }

    @AfterAll
    static void afterAllTests() {
        log.info("Finished all tests.");
    }

    @Test
    void shouldApplyPromotions() {
        //given
        final List<Integer> requestedPromotions = List.of(10, 20, 30);
        when(promotionRepository.getPromotionByIds(any()))
                .thenReturn(samplePromotions());
        //when
        final BigDecimal priceAfterPromotions = promotionService.calculatePriceAfterPromotions(requestedPromotions, _1000);

        //then
        assertThat(priceAfterPromotions).isEqualTo(new BigDecimal("580.00"));
    }

    @Test
    void shouldReturnInitialPriceWhenNoPromotions() {
        //given
        final List<Integer> requestedPromotions = List.of(10, 20, 30);
        when(promotionRepository.getPromotionByIds(any()))
                .thenReturn(List.of());
        //when
        final BigDecimal priceAfterPromotions = promotionService.calculatePriceAfterPromotions(requestedPromotions, _1000);

        //then
        assertThat(priceAfterPromotions).isEqualTo(_1000);
    }


    private static List<Promotion> samplePromotions() {
        return List.of(PercentagePromotion.of(10, "Student promo", BigDecimal.valueOf(0.20)),
                       PercentagePromotion.of(20, "Black Friday promo", BigDecimal.valueOf(0.10)),
                       AmountPromotion.of(30, "Chefs nephew promo", BigDecimal.valueOf(100)),
                       AmountPromotion.of(40, "birthday promo", BigDecimal.valueOf(40)));
    }
}
