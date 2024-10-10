package ru.customer.petproject.customerservice.unittests.util;

import ru.zinovievbank.customerservice.model.Address;

public class MockClassAddressForCustomerGrpcService {

    private static final String COUNTRY = "Russia";
    private static final String REGION = "Russia";
    private static final String CITY = "Москва";
    private static final String STREET = "Ленинградская";
    private static final String HOUSE_NUMBER = "9";
    private static final String ENTRANCE_NUMBER = "1";
    private static final Integer APARTMENT_NUMBER = 78;
    private static final Integer POSTCODE = 119330;
    private static final String OKTMO = "12345678910";

    public Address getAddress() {
        Address address = new Address();
        address.setCountry(COUNTRY);
        address.setCity(CITY);
        address.setStreet(STREET);
        address.setRegion(REGION);
        address.setEntranceNumber(ENTRANCE_NUMBER);
        address.setPostcode(POSTCODE);
        address.setApartmentNumber(APARTMENT_NUMBER);
        address.setHouseNumber(HOUSE_NUMBER);
        address.setOktmo(OKTMO);
        return address;
    }
}
