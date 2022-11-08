package pl.com.softko.workshops.tests.testsworkshop.loan;

import java.time.LocalDate;

public record LoanOrderClient(long id, String name, String surname, String pesel, String personalId,
                              LocalDate birthDate) {
}
