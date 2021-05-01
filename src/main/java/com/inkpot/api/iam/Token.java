package com.inkpot.api.iam;

import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.util.KeyUtils;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.lang.JoseException;

import java.util.Objects;

import static com.inkpot.api.iam.EncryptionUtil.readEncryptionPassword;
import static com.inkpot.api.iam.EncryptionUtil.sha256;

public final class Token {

    private final String subject;

    private Token(Builder builder) {
        this.subject = builder.subject;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getSubject() {
        return subject;
    }

    public String asString() {
        return Jwt.subject(subject).sign(KeyUtils.createSecretKeyFromSecret(readSecret()));
    }

    public static Token fromStringToken(String stringToken) {
        try {
            var jws = JsonWebSignature.fromCompactSerialization(stringToken);
            jws.setKey(KeyUtils.createSecretKeyFromSecret(readSecret()));
            var claims = JwtClaims.parse(jws.getPayload());
            return builder().subject(claims.getSubject()).build();
        } catch (InvalidJwtException | MalformedClaimException | JoseException e) {
            throw new IllegalStateException(e);
        }
    }

    public static boolean isValidStringToken(String stringToken) {
        try {
            var jws = new JsonWebSignature();
            jws.setCompactSerialization(stringToken);
            jws.setKey(KeyUtils.createSecretKeyFromSecret(readSecret()));

            return jws.verifySignature();
        } catch (JoseException e) {
            return false;
        }
    }

    private static String readSecret() {
        return sha256(readEncryptionPassword());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return Objects.equals(getSubject(), token.getSubject());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSubject());
    }
    public static final class Builder {


        private String subject;

        private Builder() {
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Token build() {
            return new Token(this);
        }

    }

}
