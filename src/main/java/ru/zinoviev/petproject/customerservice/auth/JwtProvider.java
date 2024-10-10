package ru.zinovievbank.customerservice.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
@NoArgsConstructor
public class JwtProvider {

    @Value("${application.security.jwt.secret-key}")
    private String key;

    @Value("${application.security.jwt.expiration-access}")
    private long jwtAccessExpiration;


    /**
     * Extract UUID User from token.
     *
     * @param token String as a hashed token (required)
     * @return UUID User from token as string
     */
    public UUID extractCustomerId(String token) {
        log.info("Extract customer id");
        return UUID.fromString(extractClaim(token, Claims::getSubject));
    }

    /**
     * Check token for expired.
     *
     * @param token String as a hashed token (required)
     */
    public void checkTokenExpired(String token) {
        log.info("Check tokens for expired");
        Date extractExpiration = extractExpiration(token);
        log.debug("Token expiration: {}", extractExpiration);
    }

    /**
     * Extract token data expiration.
     *
     * @param token String as a hashed token (required)
     * @return Date expiration
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    /**
     * Extract all claims.
     *
     * @param token String as a hashed token (required)
     * @return Returns the JWT body, either a Claims instance.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    /**
     * Extract claims as the function result.
     *
     * @param token          String as a hashed token (required)
     * @param <T>            The result type of the function
     * @param claimsResolver Function of claims resolver
     * @return The function result
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Get the signing key based on the secret key.
     *
     * @return sign key
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
