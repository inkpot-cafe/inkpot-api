package com.inkpot.api.iam;

import io.quarkus.security.identity.CurrentIdentityAssociation;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
class AuthenticatorTest {

    private static final User USER = UserTest.buildUser();
    private static final String WRONG_PASSWORD = "wrongPassword";
    private static final Token TOKEN = USER.generateToken();
    private static final String INVALID_TOKEN = "invalidToken";

    @Inject
    Authenticator authenticator;

    @InjectMock
    UserDao userDao;
    @InjectMock
    CurrentIdentityAssociation currentIdentityAssociation;

    @BeforeEach
    void setUp() {
        when(userDao.readUser(UserTest.USERNAME)).thenReturn(Optional.of(USER));
    }

    @Test
    void authenticateUsernamePassword() throws AuthenticationException {
        var user = authenticator.authenticate(UserTest.USERNAME, UserTest.PASSWORD);

        verify(userDao).readUser(UserTest.USERNAME);
        assertThat(user).isEqualTo(USER);
    }

    @Test
    void authenticateUsernamePasswordInvalid() {
        assertThatExceptionOfType(AuthenticationException.class)
                .isThrownBy(() -> authenticator.authenticate(UserTest.USERNAME, WRONG_PASSWORD));

        verify(userDao).readUser(UserTest.USERNAME);
    }

    @Test
    void authenticateToken() throws AuthenticationException {
        var user = authenticator.authenticate(TOKEN.asString());

        assertThat(user).isEqualTo(USER);
    }

    @Test
    void authenticateTokenInvalid() {
        assertThatExceptionOfType(AuthenticationException.class)
                .isThrownBy(() -> authenticator.authenticate(INVALID_TOKEN));

    }

    @Test
    void currentAuthenticatedUser() {
        when(currentIdentityAssociation.getIdentity()).thenReturn(USER.toSecurityIdentity());

        var actual = authenticator.currentAuthenticatedUser();

        assertThat(actual).isEqualTo(USER);
    }

}
