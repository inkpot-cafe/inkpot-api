package com.inkpot.api.iam.quarkus;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.security.identity.IdentityProviderManager;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.TokenAuthenticationRequest;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.vertx.http.runtime.security.HttpCredentialTransport;
import io.smallrye.mutiny.Uni;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
class TokenAuthenticationMechanismTest {

    private static final String BEARER = "bearer";
    private static final String BEARER_TOKEN = BEARER + " token";
    private static final String INVALID_HEADER = "invalidHeader";

    @Inject
    TokenAuthenticationMechanism tokenAuthenticationMechanism;

    @Mock
    RoutingContext routingContext;
    @Mock
    HttpServerRequest httpServerRequest;
    @Mock
    IdentityProviderManager identityProviderManager;
    @Mock
    SecurityIdentity securityIdentity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(routingContext.request()).thenReturn(httpServerRequest);

        when(httpServerRequest.headers()).thenReturn(MultiMap.caseInsensitiveMultiMap());
    }

    private void addBearerToken() {
        httpServerRequest.headers().add(HttpHeaderNames.AUTHORIZATION, BEARER_TOKEN);
    }

    private void addInvalidHeader() {
        httpServerRequest.headers().add(HttpHeaderNames.AUTHORIZATION, INVALID_HEADER);
    }

    private void mockIdentityProviderManagerToReturn(SecurityIdentity securityIdentity) {
        when(identityProviderManager.authenticate(any(TokenAuthenticationRequest.class))).thenReturn(Uni.createFrom().item(securityIdentity));
    }

    @Test
    void credentialTypes() {
        var credentialTypes = tokenAuthenticationMechanism.getCredentialTypes();

        assertThat(credentialTypes).containsExactly(TokenAuthenticationRequest.class);
    }

    @Test
    void credentialTransport() {
        var credentialTransport = tokenAuthenticationMechanism.getCredentialTransport();

        assertThat(credentialTransport).isEqualTo(new HttpCredentialTransport(HttpCredentialTransport.Type.AUTHORIZATION, BEARER));
    }

    @Test
    void challengeData() {
        var uni = tokenAuthenticationMechanism.getChallenge(routingContext);

        var challengeData = UniUtils.readItem(uni);

        assertThat(challengeData.status).isEqualTo(HttpResponseStatus.UNAUTHORIZED.code());
        assertThat(challengeData.headerName).isEqualTo(HttpHeaderNames.WWW_AUTHENTICATE);
        assertThat(challengeData.headerContent).isEqualTo(BEARER);
    }

    @Test
    void authenticate() {
        addBearerToken();
        mockIdentityProviderManagerToReturn(securityIdentity);

        var uni = tokenAuthenticationMechanism.authenticate(routingContext, identityProviderManager);

        var actual = UniUtils.readItem(uni);
        assertThat(actual).isEqualTo(securityIdentity);
    }

    @Test
    void authenticateNoBearerHeader() {
        var uni = tokenAuthenticationMechanism.authenticate(routingContext, identityProviderManager);

        var securityIdentity = UniUtils.readItem(uni);
        assertThat(securityIdentity).isNull();
    }

    @Test
    void authenticateInvalidToken() {
        addBearerToken();
        mockIdentityProviderManagerToReturn(null);

        var uni = tokenAuthenticationMechanism.authenticate(routingContext, identityProviderManager);

        var securityIdentity = UniUtils.readItem(uni);
        assertThat(securityIdentity).isNull();
    }

    @Test
    void authenticateInvalidHeader() {
        addInvalidHeader();

        var uni = tokenAuthenticationMechanism.authenticate(routingContext, identityProviderManager);

        var securityIdentity = UniUtils.readItem(uni);
        assertThat(securityIdentity).isNull();
    }

}
