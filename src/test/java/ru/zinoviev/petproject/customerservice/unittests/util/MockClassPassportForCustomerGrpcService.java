package ru.customer.petproject.customerservice.unittests.util;

import java.time.LocalDate;
import ru.zinovievbank.customerservice.model.Passport;

public class MockClassPassportForCustomerGrpcService {

    public static final String SERIES = "1234";
    private static final String PASSPORT_NUMBER = "145879";
    private static final String CITIZENSHIP = "145879";
    private static final LocalDate ISSUANCE_DATE = LocalDate.of(2008, 10, 30);
    private static final String DEPARTMENT_CODE = "145879";
    private static final String ISSUANCE_BY = "145879";

    public Passport getPassport() {
        Passport passport = new Passport();
        passport.setSeries(SERIES);
        passport.setNumber(PASSPORT_NUMBER);
        passport.setCitizenship(CITIZENSHIP);
        passport.setIssuanceDate(ISSUANCE_DATE);
        passport.setDepartmentCode(DEPARTMENT_CODE);
        passport.setIssuedBy(ISSUANCE_BY);
        return passport;
    }
}
