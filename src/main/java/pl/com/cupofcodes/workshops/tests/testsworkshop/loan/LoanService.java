package pl.com.cupofcodes.workshops.tests.testsworkshop.loan;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.com.cupofcodes.workshops.tests.testsworkshop.EventEmitter;
import pl.com.cupofcodes.workshops.tests.testsworkshop.client.ClientService;
import pl.com.cupofcodes.workshops.tests.testsworkshop.client.ClientType;
import pl.com.cupofcodes.workshops.tests.testsworkshop.froud.FraudVerifier;
import pl.com.cupofcodes.workshops.tests.testsworkshop.loan.esception.LoanAlreadyStartedCantModifyException;
import pl.com.cupofcodes.workshops.tests.testsworkshop.loan.esception.NumberOfInstallmentsValidationException;
import pl.com.cupofcodes.workshops.tests.testsworkshop.promotion.PromotionService;
import pl.com.cupofcodes.workshops.tests.testsworkshop.loan.esception.FraudDetectedException;
import pl.com.cupofcodes.workshops.tests.testsworkshop.loan.esception.RequestedAmountToHighValidationException;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
class LoanService {
    private static final BigDecimal FEE = BigDecimal.valueOf(200);
    private final LoanProperties loanProperties;
    private final FraudVerifier fraudVerifier;
    private final ClientService clientService;
    private final PromotionService promotionService;
    private final LoanRepository loanRepository;
    private final EventEmitter eventEmitter;

    public Loan createLoan(LoanOrder loanOrder) {
        log.info("star processing loanOrder = {}", loanOrder);
        final boolean fraud = fraudVerifier.isFraud(loanOrder.loanOrderClient());
        if (fraud) {
            log.error("found fraud for loanOrder = {}", loanOrder);
            throw new FraudDetectedException(loanOrder);
        }

        final BigDecimal priceAfterPromotions = promotionService.calculatePriceAfterPromotions(loanOrder.grantedPromotionsIds(), loanOrder.amount());
        final BigDecimal oriceAfterFess = priceAfterPromotions.add(FEE);

        final ClientType clientType = clientService.getClientType(loanOrder.loanOrderClient().id());


        switch (clientType) {
            case VIP:
                validateLoanConstraintsNotExceeded(oriceAfterFess, loanOrder.installmentsAmount(), 12, 0.25);
                break;
            case REGULAR:
                validateLoanConstraintsNotExceeded(oriceAfterFess, loanOrder.installmentsAmount(), 8, 0.15);
                break;
            case STUDENT:
                validateLoanConstraintsNotExceeded(oriceAfterFess, loanOrder.installmentsAmount(), 10, 0.10);
                break;
            case NORMAL:
                validateLoanConstraintsNotExceeded(oriceAfterFess, loanOrder.installmentsAmount());
                break;
        }
        log.info("end processing loanOrder = {}", loanOrder);
        Loan loan = new Loan(oriceAfterFess, loanOrder.installmentsAmount(), loanOrder.loanOrderClient().id());
        loanRepository.createLoan(loan);
        eventEmitter.emit(new LoanGrantedEvent(loan.getId()));
        return loan;
    }

    private void validateLoanConstraintsNotExceeded(BigDecimal calculatedPriceAfterFess, int requestedInstallments) {
        final BigDecimal maxAmountOfAllowedMoney = loanProperties.getMaxAmountOfMoney();
        final int maxAmountOfInstallments = loanProperties.getMaxAmountOfInstallments();

        if (requestedInstallments > maxAmountOfInstallments) {
            throw new NumberOfInstallmentsValidationException(requestedInstallments, maxAmountOfInstallments);
        }

        if (calculatedPriceAfterFess.compareTo(maxAmountOfAllowedMoney) > 0) {
            throw new RequestedAmountToHighValidationException(calculatedPriceAfterFess, maxAmountOfAllowedMoney);
        }
    }

    private void validateLoanConstraintsNotExceeded(BigDecimal calculatedPriceAfterFess, int requestedInstallments, int maxInstallments, double allowedExceeding) {
        final BigDecimal maxAmountOfAllowedMoney = loanProperties.getMaxAmountOfMoney();

        if (requestedInstallments > maxInstallments) {
            throw new NumberOfInstallmentsValidationException(requestedInstallments, maxInstallments);
        }
//Błąd do wykrycia w testach ważne, żeby sprawdzić każdy przypadek i tutaj branch coverage nam pokazało, że nie ma przypadku dla tego
        final BigDecimal maxAllowedWithExceeding = maxAmountOfAllowedMoney.multiply(BigDecimal.valueOf(allowedExceeding));
        if (maxAllowedWithExceeding.compareTo(calculatedPriceAfterFess) < 0) {
            throw new RequestedAmountToHighValidationException(calculatedPriceAfterFess, maxAllowedWithExceeding);
        }
    }



    public Loan changeInstallments(int newInstallmentsNumber, UUID loanId) {
        final Loan loan = loanRepository.loadLoanBy(loanId);
        if (loan.isLoanStarted()){
            throw new LoanAlreadyStartedCantModifyException("Loan already started can't change any prameters");
        }
        loan.changeInstallments(newInstallmentsNumber);
        loanRepository.updateLoan(loan);
        return loan;
    }
}
