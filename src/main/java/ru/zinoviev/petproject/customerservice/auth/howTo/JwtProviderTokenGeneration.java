package ru.zinovievbank.customerservice.auth.howTo;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtProviderTokenGeneration {

    @Value("${application.security.jwt.secret-key}")
    private String key;

    @Value("${application.security.jwt.expiration-access}")
    private long jwtAccessExpiration;


    public String generateAccessToken(UUID customerId) {
        return buildToken(new HashMap<>(), customerId, jwtAccessExpiration);
    }

    private String buildToken(Map<String, Object> extraClaims, UUID customerId, long expiration) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(customerId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
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
