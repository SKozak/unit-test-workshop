package pl.com.cupofcodes.workshops.tests.testsworkshop.promotion;

import java.util.List;

interface PromotionRepository {
    List<Promotion> getPromotionByIds(List<Integer> grantedPromotionsIds);
}
