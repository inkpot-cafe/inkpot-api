package com.inkpot.api.iam;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public final class Token {

    private final String subject;
    private final String stringValue;

    private Token(Builder builder) {
        this.subject = builder.subject;
        this.stringValue = builder.stringValue;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getSubject() {
        return subject;
    }

    public String asString() {
        return stringValue;
    }

    public static Token fromStringToken(String stringToken) {
        var jwtParser = instantiateJwtParser();
        var body = jwtParser.parseClaimsJws(stringToken).getBody();

        return builder().subject(body.getSubject()).build();
    }

    public static boolean isValidStringToken(String stringToken) {
        var jwtParser = instantiateJwtParser();

        try {
            jwtParser.parseClaimsJws(stringToken);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    private static JwtParser instantiateJwtParser() {
        return Jwts.parserBuilder().setSigningKey(readSecretKey()).build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return Objects.equals(getSubject(), token.getSubject()) && Objects.equals(stringValue, token.stringValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSubject(), stringValue);
    }

    public static final class Builder {

        private String subject;
        private String stringValue;

        private Builder() {
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Token build() {
            generateStringToken();
            return new Token(this);
        }

        private void generateStringToken() {
            var secretKey = readSecretKey();
            this.stringValue = Jwts.builder().setSubject(subject).signWith(secretKey).compact();
        }
    }

    private static SecretKey readSecretKey() {
        return Keys.hmacShaKeyFor(EncryptionUtil.sha512Key().getBytes(StandardCharsets.UTF_8));
    }

}
