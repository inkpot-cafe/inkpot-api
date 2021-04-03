package com.inkpot.api.iam.quarkus;

import io.quarkus.security.identity.request.UsernamePasswordAuthenticationRequest;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class BasicIdentityProviderTest {

    @Inject
    BasicIdentityProvider basicIdentityProvider;

    @Test
    void testRequestType() {
        var requestType = basicIdentityProvider.getRequestType();

        assertThat(requestType).isEqualTo(UsernamePasswordAuthenticationRequest.class);
    }
}
