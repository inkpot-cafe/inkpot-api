package com.inkpot.api.iam.quarkus;

import com.inkpot.api.iam.AuthenticationException;
import com.inkpot.api.iam.Authenticator;
import com.inkpot.api.iam.User;
import com.inkpot.api.iam.UserTest;
import io.quarkus.security.AuthenticationFailedException;
import io.quarkus.security.credential.PasswordCredential;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.request.UsernamePasswordAuthenticationRequest;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@QuarkusTest
class BasicIdentityProviderTest {

    private static final User USER = UserTest.buildUser();
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String INVALID_PASSWORD = "invalidPassword";

    @Inject
    BasicIdentityProvider basicIdentityProvider;

    @InjectMock
    Authenticator authenticator;

    @Mock
    AuthenticationRequestContext context;

    @Test
    void testRequestType() {
        var requestType = basicIdentityProvider.getRequestType();

        assertThat(requestType).isEqualTo(UsernamePasswordAuthenticationRequest.class);
    }

    @Test
    void authenticate() throws AuthenticationException {
        when(authenticator.authenticate(USERNAME, PASSWORD)).thenReturn(USER);

        var uni = basicIdentityProvider.authenticate(request(PASSWORD), context);

        var securityIdentity = UniUtils.readItem(uni);

        UserTest.assertSecurityIdentity(securityIdentity);
    }

    @Test
    void invalidAuthenticate() throws AuthenticationException {
        when(authenticator.authenticate(USERNAME, INVALID_PASSWORD)).thenThrow(new AuthenticationException(""));

        assertThatExceptionOfType(AuthenticationFailedException.class)
                .isThrownBy(() -> basicIdentityProvider.authenticate(request(INVALID_PASSWORD), context));
    }

    private UsernamePasswordAuthenticationRequest request(String invalidPassword) {
        return new UsernamePasswordAuthenticationRequest(USERNAME, new PasswordCredential(invalidPassword.toCharArray()));
    }

}
