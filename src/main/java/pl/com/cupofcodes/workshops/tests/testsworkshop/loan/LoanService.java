package pl.com.cupofcodes.workshops.tests.testsworkshop.loan;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.com.cupofcodes.workshops.tests.testsworkshop.EventEmitter;
import pl.com.cupofcodes.workshops.tests.testsworkshop.froud.FraudVerifier;
import pl.com.cupofcodes.workshops.tests.testsworkshop.loan.esception.FraudDetectedException;
import pl.com.cupofcodes.workshops.tests.testsworkshop.loan.esception.LoanAlreadyStartedCantModifyException;
import pl.com.cupofcodes.workshops.tests.testsworkshop.promotion.PromotionService;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
class LoanService {
    private static final BigDecimal FEE = BigDecimal.valueOf(200);
    private final FraudVerifier fraudVerifier;
    private final PromotionService promotionService;
    private final LoanRepository loanRepository;
    private final EventEmitter eventEmitter;
    private final LoanConstraintValidator loanConstraintValidator;

    public Loan createLoan(LoanOrder loanOrder) {
        log.info("star processing loanOrder = {}", loanOrder);
        final boolean fraud = fraudVerifier.isFraud(loanOrder.loanOrderClient());
        if (fraud) {
            log.error("found fraud for loanOrder = {}", loanOrder);
            throw new FraudDetectedException(loanOrder);
        }

        final BigDecimal priceAfterPromotions = promotionService.calculatePriceAfterPromotions(loanOrder.grantedPromotionsIds(), loanOrder.amount());
        final BigDecimal priceAfterFess = priceAfterPromotions.add(FEE);

        loanConstraintValidator.validateConstraints(loanOrder.loanOrderClient()
                                                  .id(), priceAfterFess, loanOrder.installmentsAmount());

        log.info("end processing loanOrder = {}", loanOrder);
        Loan loan = new Loan(priceAfterFess, loanOrder.installmentsAmount(), loanOrder.loanOrderClient().id());
        loanRepository.createLoan(loan);
        eventEmitter.emit(new LoanGrantedEvent(loan.getId()));
        return loan;
    }

    public Loan changeInstallments(int newInstallmentsNumber, UUID loanId) {
        final Loan loan = loanRepository.loadLoanBy(loanId);
        if (loan.isLoanStarted()) {
            throw new LoanAlreadyStartedCantModifyException("Loan already started can't change any prameters");
        }
        loan.changeInstallments(newInstallmentsNumber);
        loanRepository.updateLoan(loan);
        return loan;
    }
}
