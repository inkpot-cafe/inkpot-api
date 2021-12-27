package com.inkpot.api.iam;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.jwt.util.KeyUtils;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.Test;

import static com.inkpot.api.iam.EncryptionUtil.readEncryptionPassword;
import static com.inkpot.api.iam.EncryptionUtil.sha256;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class TokenTest {

    private static final String SUBJECT = "subject";
    private static final String INVALID_TOKEN_STRING = "invalidTokenString";

    private static Token buildToken() {
        return Token.builder()
                .subject(SUBJECT)
                .build();
    }

    @Test
    void builder() throws JoseException, InvalidJwtException, MalformedClaimException {
        var token = buildToken();

        assertThat(token.getSubject()).isEqualTo(SUBJECT);
        assertThat(token.asString()).isNotBlank();

        var jws = JsonWebSignature.fromCompactSerialization(token.asString());
        jws.setKey(KeyUtils.createSecretKeyFromSecret(sha256(readEncryptionPassword())));
        var claims = JwtClaims.parse(jws.getPayload());
        assertThat(claims.getSubject()).isEqualTo(SUBJECT);
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
