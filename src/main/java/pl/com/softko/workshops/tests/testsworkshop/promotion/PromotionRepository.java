package pl.com.softko.workshops.tests.testsworkshop.promotion;

import java.util.List;

interface PromotionRepository {
    List<Promotion> getPromotionByIds(List<Integer> grantedPromotionsIds);
}
