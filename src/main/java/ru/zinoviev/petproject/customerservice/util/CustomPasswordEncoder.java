package ru.zinovievbank.customerservice.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.aston.globalexeptionsspringbootstarter.exception.AppException;
import ru.aston.globalexeptionsspringbootstarter.exception.EnumException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for irreversible password hashing.
 * Changing methods in a class or influencing the behavior of the class in any way is prohibited!
 */
@Slf4j
public class CustomPasswordEncoder implements PasswordEncoder {

    /**
     * Method for encoding a password with 'salt', 'pepper' and hashing using the SHA-256 algorithm.
     *
     * @param password Password as a byte array (required)
     * @return String represented as hashed password with 'salt' and 'pepper'
     */
//    public static String encodePassword(byte[] password) {
//        log.debug("Start to encode password");
//        byte[] pswWithSalt = getPswWithSalt(password);
//        pswWithSalt = getPswHash(pswWithSalt);
//        return bytesToHexWithPepper(pswWithSalt);
//    }

    /**
     * Method of adding 'salt' to password.
     * Expressed in changing each element in the password byte array according to a certain formula.
     *
     * @param psw Password as a byte array (required)
     * @return Array 'of salted' bytes
     */
    private byte[] getPswWithSalt(byte[] psw) {
        log.debug("Add salt to password");
        for (int i = 0; i < psw.length; i++) {
            byte sqrt = (byte) Math.sqrt(psw[i]);
            psw[i] = (byte) Math.abs(psw[i] + sqrt);
        }
        return psw;
    }

    /**
     * Password hashing method.
     * Expressed as hashing a byte array using the SHA-256 algorithm.
     *
     * @param pswWithSalt Array 'of salted' bytes (required)
     * @return Hashed byte array
     */
    private byte[] getPswHash(byte[] pswWithSalt) {
        log.debug("Get hash from password with salt");
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(pswWithSalt);
        } catch (NoSuchAlgorithmException e) {
            log.error("Can not get hash from password");
            throw new AppException(EnumException.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Method for converting bytes to hex with 'pepper' added.
     * It is expressed in changing each element of the hash byte array according to a certain formula and converting it into hexadecimal format.
     *
     * @param hash Hashed byte array (required)
     * @return Hashed string in hexadecimal format
     */
    private String bytesToHexWithPepper(byte[] hash) {
        log.debug("Convert byte hash in hex hash with pepper");
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            byte sqrt = (byte) Math.sqrt(b);
            String hex = Integer.toHexString(Math.abs(b - sqrt));
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    @Override
    public String encode(CharSequence rawPassword) {
        log.debug("Start to encode password");
        byte[] pswWithSalt = getPswWithSalt(rawPassword.toString().getBytes());
        pswWithSalt = getPswHash(pswWithSalt);
        return bytesToHexWithPepper(pswWithSalt);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null) {
            //todo:Посмотреть нужный exc
            throw new IllegalArgumentException("rawPassword cannot be null");
        }
        if (encodedPassword == null || encodedPassword.isEmpty()) {
            log.warn("Empty encoded password");
            return false;
        }
//        if (!this.BCRYPT_PATTERN.matcher(encodedPassword).matches()) {
//            this.logger.warn("Encoded password does not look like BCrypt");
//            return false;
//        }
        return true;
    }

}
