package pl.com.cupofcodes.workshops.tests.testsworkshop.promotion;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
class InMemoryPromotionRepository implements PromotionRepository {
    private final Map<Integer, Promotion> PROMOTIONS_MAP = Map.of(
            20, PercentagePromotion.of(20, "Student promo", BigDecimal.valueOf(0.20)),
            10, PercentagePromotion.of(20, "Black Friday promo", BigDecimal.valueOf(0.05)),
            22, PercentagePromotion.of(20, "Chefs nephew promo", BigDecimal.valueOf(0.10)),
            2, PercentagePromotion.of(20, "birthday promo", BigDecimal.valueOf(0.04))
    );

    @Override
    public List<Promotion> getPromotionByIds(List<Integer> grantedPromotionsIds) {
        return PROMOTIONS_MAP.entrySet().stream()
                .filter(entry -> grantedPromotionsIds.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
}
