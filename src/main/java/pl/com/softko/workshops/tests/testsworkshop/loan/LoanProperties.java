package pl.com.softko.workshops.tests.testsworkshop.loan;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Data
@Configuration
@ConfigurationProperties(prefix = "loan")
class LoanProperties {
    private BigDecimal maxAmountOfMoney;
    private int maxAmountOfInstallments;
}
