package ru.customer.petproject.customerservice.unittests.util;

import com.google.protobuf.Timestamp;
import ru.aston.grpc.server.CustomerResponse;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class MockClassCustomResponse {
    private final static String CUSTOMER_UUID = "7f89ff6d-452e-4ecd-8112-2ed19bf78184";
    private final static String FIRST_NAME = "Дмитрий";
    private final static String LAST_NAME = "Петров";
    private final static String PATRONYMIC = "Владимирович";
    private final static String MOBILE_PHONE = "79808901750";
    private final static String OFFICE_REG_UUID = "dc9e2072-5d22-11ee-8c99-0242ac120002";


    private final static String dateString = "2024-04-05";

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final static LocalDate localDate = LocalDate.parse(dateString, formatter);

    private final static Instant TIME = localDate.atStartOfDay().toInstant(ZoneOffset.UTC);
    private final static Timestamp timestamp = Timestamp.newBuilder().setSeconds(TIME.getEpochSecond())
            .setNanos(TIME.getNano()).build();


    private final static Timestamp ACCESION_DATE = timestamp;
    private final static String EMAIL = "dmitriy.petrov567@yandex.com";
    private final static Timestamp BIRTH_DATE = timestamp;
    private final static String INN = "123410089104";
    private final static String COUNTRY = "Россия";
    private final static String REGION = "Московская область";
    private final static String CITY = "Балашиха";
    private final static String STREET = "Кремлевская";
    private final static String HOUSE_NUMBER = "58";
    private final static int ENTRANCE_NUMBER = 5;
    private final static int APARTMENT_NUMBER = 7;
    private final static int POSTCODE = 140142;
    private final static String OKTMO = "12345678940";
    private final static String CODEWORD = "Андрей";
    private static final boolean SMS_NOTIFICATION = false;
    private static final boolean PUSH_NOTIFICATION = true;
    private static final boolean EMAIL_NOTIFICATION = false;
    private static final String CLIENT_STATUS = "CLIENT_SDBO";
    private static final String SECURITY_QUESTION = "Что измеряет вольтметр?";
    private static final String SECURITY_ANSWER = "Напряжение";
    private static final Timestamp DATE_OF_ISSUE = timestamp;
    private static final String ISSUED_BY = "Отделом УФМС России по г. Казань";
    private static final String DEPARTAMENT_CODE = "204-120";
    private static final String CITIZENSHIP = "RU";
    private static final String SERIES = "7614";
    private static final String NUMBER = "901238";

    public CustomerResponse getCustomerResponseWithAllFields() {

        return CustomerResponse.newBuilder()
                .setCustomerUuid(CUSTOMER_UUID)
                .setFirstName(FIRST_NAME)
                .setLastName(LAST_NAME)
                .setPatronymic(PATRONYMIC)
                .setMobilePhone(MOBILE_PHONE)
                .setOfficeRegUuid(OFFICE_REG_UUID)
                .setAccesionDate(ACCESION_DATE)
                .setEmail(EMAIL)
                .setBirthDate(BIRTH_DATE)
                .setInn(INN)
                .setCountry(COUNTRY)
                .setRegion(REGION)
                .setCity(CITY)
                .setStreet(STREET)
                .setHouseNumber(HOUSE_NUMBER)
                .setEntranceNumber(ENTRANCE_NUMBER)
                .setApartmentNumber(APARTMENT_NUMBER)
                .setPostcode(POSTCODE)
                .setOktmo(OKTMO)
                .setCodeword(CODEWORD)
                .setSmsNotification(SMS_NOTIFICATION)
                .setPushNotification(PUSH_NOTIFICATION)
                .setEmailSubscription(EMAIL_NOTIFICATION)
                .setClientStatus(CLIENT_STATUS)
                .setSecurityQuestion(SECURITY_QUESTION)
                .setSecurityAnswer(SECURITY_ANSWER)
                .setDateOfIssue(DATE_OF_ISSUE)
                .setIssuedBy(ISSUED_BY)
                .setDepartamentCode(DEPARTAMENT_CODE)
                .setCitizenship(CITIZENSHIP)
                .setSeries(SERIES)
                .setNumber(NUMBER)
                .build();
    }
    public CustomerResponse getCustomerResponseWithNotRegisteredStatus() {

        return CustomerResponse.newBuilder()
                .setCustomerUuid(CUSTOMER_UUID)
                .setFirstName(FIRST_NAME)
                .setLastName(LAST_NAME)
                .setPatronymic(PATRONYMIC)
                .setMobilePhone(MOBILE_PHONE)
                .setOfficeRegUuid(OFFICE_REG_UUID)
                .setAccesionDate(ACCESION_DATE)
                .setEmail(EMAIL)
                .setBirthDate(BIRTH_DATE)
                .setInn(INN)
                .setCountry(COUNTRY)
                .setRegion(REGION)
                .setCity(CITY)
                .setStreet(STREET)
                .setHouseNumber(HOUSE_NUMBER)
                .setEntranceNumber(ENTRANCE_NUMBER)
                .setApartmentNumber(APARTMENT_NUMBER)
                .setPostcode(POSTCODE)
                .setOktmo(OKTMO)
                .setCodeword(CODEWORD)
                .setSmsNotification(SMS_NOTIFICATION)
                .setPushNotification(PUSH_NOTIFICATION)
                .setEmailSubscription(EMAIL_NOTIFICATION)
                .setClientStatus("CLIENT_NOT_SDBO")
                .setSecurityQuestion(SECURITY_QUESTION)
                .setSecurityAnswer(SECURITY_ANSWER)
                .setDateOfIssue(DATE_OF_ISSUE)
                .setIssuedBy(ISSUED_BY)
                .setDepartamentCode(DEPARTAMENT_CODE)
                .setCitizenship(CITIZENSHIP)
                .setSeries(SERIES)
                .setNumber(NUMBER)
                .build();
    }
    public CustomerResponse getCustomerResponseWithBlockedStatus() {

        return CustomerResponse.newBuilder()
                .setCustomerUuid(CUSTOMER_UUID)
                .setFirstName(FIRST_NAME)
                .setLastName(LAST_NAME)
                .setPatronymic(PATRONYMIC)
                .setMobilePhone(MOBILE_PHONE)
                .setOfficeRegUuid(OFFICE_REG_UUID)
                .setAccesionDate(ACCESION_DATE)
                .setEmail(EMAIL)
                .setBirthDate(BIRTH_DATE)
                .setInn(INN)
                .setCountry(COUNTRY)
                .setRegion(REGION)
                .setCity(CITY)
                .setStreet(STREET)
                .setHouseNumber(HOUSE_NUMBER)
                .setEntranceNumber(ENTRANCE_NUMBER)
                .setApartmentNumber(APARTMENT_NUMBER)
                .setPostcode(POSTCODE)
                .setOktmo(OKTMO)
                .setCodeword(CODEWORD)
                .setSmsNotification(SMS_NOTIFICATION)
                .setPushNotification(PUSH_NOTIFICATION)
                .setEmailSubscription(EMAIL_NOTIFICATION)
                .setClientStatus("CLIENT_BLOCKED")
                .setSecurityQuestion(SECURITY_QUESTION)
                .setSecurityAnswer(SECURITY_ANSWER)
                .setDateOfIssue(DATE_OF_ISSUE)
                .setIssuedBy(ISSUED_BY)
                .setDepartamentCode(DEPARTAMENT_CODE)
                .setCitizenship(CITIZENSHIP)
                .setSeries(SERIES)
                .setNumber(NUMBER)
                .build();
    }
    public CustomerResponse getCustomerResponseWithIncorrectStatus() {

        return CustomerResponse.newBuilder()
                .setCustomerUuid(CUSTOMER_UUID)
                .setFirstName(FIRST_NAME)
                .setLastName(LAST_NAME)
                .setPatronymic(PATRONYMIC)
                .setMobilePhone(MOBILE_PHONE)
                .setOfficeRegUuid(OFFICE_REG_UUID)
                .setAccesionDate(ACCESION_DATE)
                .setEmail(EMAIL)
                .setBirthDate(BIRTH_DATE)
                .setInn(INN)
                .setCountry(COUNTRY)
                .setRegion(REGION)
                .setCity(CITY)
                .setStreet(STREET)
                .setHouseNumber(HOUSE_NUMBER)
                .setEntranceNumber(ENTRANCE_NUMBER)
                .setApartmentNumber(APARTMENT_NUMBER)
                .setPostcode(POSTCODE)
                .setOktmo(OKTMO)
                .setCodeword(CODEWORD)
                .setSmsNotification(SMS_NOTIFICATION)
                .setPushNotification(PUSH_NOTIFICATION)
                .setEmailSubscription(EMAIL_NOTIFICATION)
                .setClientStatus("random_status")
                .setSecurityQuestion(SECURITY_QUESTION)
                .setSecurityAnswer(SECURITY_ANSWER)
                .setDateOfIssue(DATE_OF_ISSUE)
                .setIssuedBy(ISSUED_BY)
                .setDepartamentCode(DEPARTAMENT_CODE)
                .setCitizenship(CITIZENSHIP)
                .setSeries(SERIES)
                .setNumber(NUMBER)
                .build();
    }
}
