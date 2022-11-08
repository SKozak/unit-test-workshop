package pl.com.softko.workshops.tests.testsworkshop.promotion;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
public class PromotionService {
    private final PromotionRepository promotionRepository;

    public BigDecimal calculatePriceAfterPromotions(List<Integer> grantedPromotionsIds, BigDecimal amount) {
        final List<Promotion> activePromotions = promotionRepository.getPromotionByIds(grantedPromotionsIds);
        BigDecimal priceAfterPromotions = amount;
        for (Promotion activePromotion : activePromotions) {
            priceAfterPromotions = activePromotion.applyFor(priceAfterPromotions);
        }
        return priceAfterPromotions;
    }
}
