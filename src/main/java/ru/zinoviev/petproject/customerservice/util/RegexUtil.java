package ru.zinovievbank.customerservice.util;

public class RegexUtil {

    private static final String LOGIN = "^(?![- ._])[a-zA-Z0-9_-]{2,29}(?:\\.[a-zA-Z0-9_-]{1,28})*@";
    private static final String DOMAIN_WITHOUT_IP = "[a-zA-Z0-9_-]{2,15}\\.[a-zA-Z]{2,3}$";
    public static final String EMAIL = LOGIN + DOMAIN_WITHOUT_IP;
    public static final String MOBILE_PHONE = "7[0-9]{10}";
    public static final String PASSPORT_NUMBER = "[0-9]{10}";
    public static final String SECRET_QUESTION_AND_ANSWER = "^[[a-zA-Z0-9а-яА-ЯёЁ!?,._-]+( [a-zA-Z0-9а-яА-ЯёЁ!?,._-]+)*]{3,50}$";
    public static final String TOKEN_REGEX = "(^[A-Za-z0-9-_]*\\.[A-Za-z0-9-_]*\\.[A-Za-z0-9-_]*$)";
    public static final String LOGIN_REGEX = "^7?\\d{10}$";

    private RegexUtil() {
    }
}
