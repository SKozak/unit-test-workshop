package pl.com.cupofcodes.workshops.tests.testsworkshop.loan;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/loan")
@RequiredArgsConstructor
class LoanController {
    private final LoanService loanService;

    @PostMapping()
    public Loan requestLoan(@Validated LoanOrder loanOrder) {
        return loanService.createLoan(loanOrder);
    }
}
