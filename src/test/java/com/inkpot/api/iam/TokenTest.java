package com.inkpot.api.iam;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class TokenTest {

    private static final String SUBJECT = "subject";
    private static final String INVALID_TOKEN_STRING = "invalidTokenString";
    private JwtParser jwtParser;

    @BeforeEach
    void setUp() {
        jwtParser = newJwtParser();
    }

    private JwtParser newJwtParser() {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(EncryptionUtil.sha512Key().getBytes(StandardCharsets.UTF_8)))
                .build();
    }

    private static Token buildToken() {
        return Token.builder()
                .subject(SUBJECT)
                .build();
    }

    @Test
    void builder() {
        var token = buildToken();

        assertThat(token.getSubject()).isEqualTo(SUBJECT);
        assertThat(token.asString()).isNotBlank();
        assertThat(jwtParser.parseClaimsJws(token.asString()).getBody().getSubject()).isEqualTo(SUBJECT);
    }

    @Test
    void fromStringToken() {
        var originalToken = buildToken();
        var tokenStr = originalToken.asString();

        var tokenFromStr = Token.fromStringToken(tokenStr);

        assertThat(tokenFromStr).isEqualTo(originalToken);
    }

    @Test
    void isValidStringToken() {
        var token = buildToken();

        var isValid = Token.isValidStringToken(token.asString());

        assertThat(isValid).isTrue();
    }

    @Test
    void isNotValidStringToken() {
        var isValid = Token.isValidStringToken(INVALID_TOKEN_STRING);

        assertThat(isValid).isFalse();
    }
}
