package pl.com.cupofcodes.workshops.tests.testsworkshop.promotion

import groovy.util.logging.Slf4j
import spock.lang.Specification

import static org.assertj.core.api.Assertions.assertThat

@Slf4j
class PromotionServiceSpec extends Specification {
    private static final BigDecimal _1000 = new BigDecimal(1000)

    def promotionRepository = Stub(PromotionRepository)
    def promotionService = new PromotionService(promotionRepository)

    def setupSpec() {
        log.info("Running all tests.")
    }

    def cleanupSpec() {
        log.info("Finished all tests.")
    }

    def "should apply promotions"() {
        given:
            final List<Integer> requestedPromotions = [10, 20, 30]
            promotionRepository.getPromotionByIds(_ as List) >> samplePromotions()
        when:
            final BigDecimal priceAfterPromotions = promotionService.calculatePriceAfterPromotions(requestedPromotions, _1000)
        then:
            priceAfterPromotions == new BigDecimal("580.00")
    }

    def "should return initial price when no promotions"() {
        given:
            final List<Integer> requestedPromotions = [20, 60, 302]
            promotionRepository.getPromotionByIds(_ as List) >> []
        when:
            final BigDecimal priceAfterPromotions = promotionService.calculatePriceAfterPromotions(requestedPromotions, _1000)
        then:
            assertThat(priceAfterPromotions).isEqualTo(_1000)
    }


    private static List<Promotion> samplePromotions() {
        return [
                PercentagePromotion.of(10, "Student promo", BigDecimal.valueOf(0.20)),
                PercentagePromotion.of(20, "Black Friday promo", BigDecimal.valueOf(0.10)),
                AmountPromotion.of(30, "Chefs nephew promo", BigDecimal.valueOf(100)),
                AmountPromotion.of(40, "birthday promo", BigDecimal.valueOf(40))
        ]
    }
}
