package com.inkpot.api.iam;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
class AuthenticatorTest {

    private static final User USER = UserTest.user();
    private static final String WRONG_PASSWORD = "wrongPassword";

    @Inject
    Authenticator authenticator;

    @InjectMock
    AuthStore authStore;

    @Test
    void authenticateUsernamePassword() throws AuthenticationException {
        when(authStore.readUser(UserTest.USERNAME)).thenReturn(USER);

        var user = authenticator.authenticate(UserTest.USERNAME, UserTest.PASSWORD);

        verify(authStore).readUser(UserTest.USERNAME);
        assertThat(user).isEqualTo(USER);
    }

    @Test
    void authenticateUsernamePasswordInvalid() {
        when(authStore.readUser(UserTest.USERNAME)).thenReturn(USER);

        Assertions.assertThatExceptionOfType(AuthenticationException.class)
                .isThrownBy(() -> authenticator.authenticate(UserTest.USERNAME, WRONG_PASSWORD));

        verify(authStore).readUser(UserTest.USERNAME);
    }

}
