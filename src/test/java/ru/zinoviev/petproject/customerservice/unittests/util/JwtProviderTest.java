package ru.customer.petproject.customerservice.unittests.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ru.zinovievbank.customerservice.auth.JwtProvider;
import ru.zinovievbank.customerservice.util.RegexUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtProviderTest {
//  private JwtProvider jwtProvider;
//
//  @BeforeEach
//  void setUp() {
//    jwtProvider = new JwtProvider(
//        "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970",
//        1000L,
//        1000L,
//        3000L
//    );
//  }
//
//  @Test
//  @DisplayName("Generate session token test")
//  void testGenerateSessionToken() {
//    UUID customerId = UUID.randomUUID();
//
//    String token = jwtProvider.generateSessionToken(customerId);
//
//    assertNotNull(token);
//    assertTrue(token.matches(RegexUtil.TOKEN_REGEX));
//  }
//
//  @Test
//  @DisplayName("Generate access token test")
//  void testGenerateAccessToken() {
//    UUID customerId = UUID.randomUUID();
//
//    String token = jwtProvider.generateAccessToken(customerId);
//
//    assertNotNull(token);
//    assertTrue(token.matches(RegexUtil.TOKEN_REGEX));
//  }
//
//  @Test
//  @DisplayName("Generate refresh token test")
//  void testGenerateRefreshToken() {
//    UUID customerId = UUID.randomUUID();
//
//    String token = jwtProvider.generateRefreshToken(customerId);
//
//    assertNotNull(token);
//    assertTrue(token.matches(RegexUtil.TOKEN_REGEX));
//  }
//
//  @Test
//  @DisplayName("Check extract customer id from token")
//  void testExtractCustomerId() {
//    UUID expectedCustomerId = UUID.randomUUID();
//    String token = jwtProvider.generateSessionToken(expectedCustomerId);
//
//    UUID extractedCustomerId = jwtProvider.extractCustomerId(token);
//
//    assertEquals(expectedCustomerId, extractedCustomerId);
//  }
//
//  @Test
//  @DisplayName("Check when token expired")
//  void testCheckTokenExpired() {
//    assertThrows(ExpiredJwtException.class,
//        () -> jwtProvider.checkTokenExpired(EXPIRED_TOKEN));
//  }
}
