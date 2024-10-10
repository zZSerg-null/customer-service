package ru.customer.petproject.customerservice.integrationtests;

import com.redis.testcontainers.RedisContainer;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cache.CacheManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.customer.petproject.customerservice.config.TestConfig;

import java.security.Key;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = TestConfig.class)
@ActiveProfiles("test")
abstract class AbstractIntegrationTest {

    @LocalServerPort
    protected Integer port;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @AfterEach
    void clear() {
        cacheManager.getCacheNames()
                .forEach(cacheName -> Objects.requireNonNull(cacheManager.getCache(cacheName)).clear());
    }

    @Test
    @DisplayName("Postgres test container start")
    void checkPostgresContainerIsRunning() {
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    @DisplayName("Kafka test container start")
    void checkKafkaContainerIsRunning() {
        assertThat(kafka.isRunning()).isTrue();
    }

    @Test
    @DisplayName("Redis test container start")
    void checkRedisContainerIsRunning() {
        assertThat(redis.isRunning()).isTrue();
    }

    protected String generateAccessToken(UUID customerId) {
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

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
