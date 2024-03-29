package com.inkpot.api.iam.quarkus;

import com.inkpot.api.iam.AuthenticationException;
import com.inkpot.api.iam.Authenticator;
import com.inkpot.api.iam.User;
import com.inkpot.api.iam.UserTest;
import io.quarkus.security.AuthenticationFailedException;
import io.quarkus.security.credential.TokenCredential;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.request.TokenAuthenticationRequest;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@QuarkusTest
class TokenIdentityProviderTest {

    private static final User USER = UserTest.buildUser();
    private static final String INVALID_TOKEN_STRING = "invalidToken";

    @Inject
    TokenIdentityProvider tokenIdentityProvider;

    @InjectMock
    Authenticator authenticator;

    @Mock
    AuthenticationRequestContext context;

    @Test
    void requestType() {
        var requestType = tokenIdentityProvider.getRequestType();

        assertThat(requestType).isEqualTo(TokenAuthenticationRequest.class);
    }

    @Test
    void authenticate() throws AuthenticationException {
        when(authenticator.authenticate(anyString())).thenReturn(USER);

        var uni = tokenIdentityProvider.authenticate(request("validToken"), context);

        var securityIdentity = UniUtils.readItem(uni);

        UserTest.assertSecurityIdentity(securityIdentity);
    }

    @Test
    void invalidAuthenticate() throws AuthenticationException {
        when(authenticator.authenticate(INVALID_TOKEN_STRING)).thenThrow(new AuthenticationException(""));

        assertThatExceptionOfType(AuthenticationFailedException.class)
                .isThrownBy(() -> tokenIdentityProvider.authenticate(request(INVALID_TOKEN_STRING), context));
    }

    private TokenAuthenticationRequest request(String tokenString) {
        return new TokenAuthenticationRequest(new TokenCredential(tokenString, null));
    }

}
