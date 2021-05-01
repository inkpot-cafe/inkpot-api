package com.inkpot.api.iam;

import io.quarkus.security.credential.PasswordCredential;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.inkpot.api.iam.EncryptionUtil.sha256;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class UserTest {

    public static final UUID AUTHOR_ID = UUID.randomUUID();
    public static final String PASSWORD = "password";
    public static final String USERNAME = "username";
    public static final String ENCRYPTED_PASSWORD = sha256(PASSWORD);

    @Test
    void builder() {
        var user = buildUser();

        assertThat(user.getAuthorId()).isEqualTo(AUTHOR_ID);
        assertThat(user.getUsername()).isEqualTo(USERNAME);
        assertThat(user.getEncryptedPassword()).isEqualTo(ENCRYPTED_PASSWORD);
    }

    @Test
    void newSecurityIdentity() {
        var user = buildUser();

        var securityIdentity = user.toSecurityIdentity();

        assertSecurityIdentity(securityIdentity);
    }

    @Test
    void generateToken() {
        var user = buildUser();

        var token = user.generateToken();

        assertThat(token).isNotNull();
        assertThat(token.getSubject()).isEqualTo(USERNAME);
    }

    @Test
    void usernameFromToken() {
        var user = buildUser();

        var token = user.generateToken();

        var username = User.readUsernameFrom(token);
        assertThat(username).isEqualTo(USERNAME);
    }

    public static User buildUser() {
        return User.builder()
                .authorId(UserTest.AUTHOR_ID)
                .username(UserTest.USERNAME)
                .encryptedPassword(ENCRYPTED_PASSWORD)
                .build();
    }

    public static void assertSecurityIdentity(io.quarkus.security.identity.SecurityIdentity securityIdentity) {
        assertThat(securityIdentity.getPrincipal().getName()).isEqualTo(USERNAME);
        assertThat(securityIdentity.getCredential(PasswordCredential.class).getPassword()).isEqualTo(ENCRYPTED_PASSWORD.toCharArray());
        assertThat(securityIdentity.<UUID>getAttribute(User.Attribute.AUTHOR_ID)).isEqualTo(AUTHOR_ID);
        assertThat(securityIdentity.<String>getAttribute(User.Attribute.USERNAME)).isEqualTo(USERNAME);
        assertThat(securityIdentity.<String>getAttribute(User.Attribute.ENCRYPTED_PASSWORD)).isEqualTo(ENCRYPTED_PASSWORD);
    }

}
