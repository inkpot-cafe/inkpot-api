package com.inkpot.api.iam;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.security.credential.TokenCredential;
import io.quarkus.security.identity.IdentityProviderManager;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.AuthenticationRequest;
import io.quarkus.security.identity.request.TokenAuthenticationRequest;
import io.quarkus.vertx.http.runtime.security.ChallengeData;
import io.quarkus.vertx.http.runtime.security.HttpAuthenticationMechanism;
import io.quarkus.vertx.http.runtime.security.HttpCredentialTransport;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.RoutingContext;

import javax.inject.Singleton;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

@Singleton
public class TokenAuthenticationMechanism implements HttpAuthenticationMechanism {

    private static final String BEARER = "bearer";
    private static final String BEARER_PREFIX = BEARER + " ";
    private static final String LOWERCASE_BEARER_PREFIX = BEARER_PREFIX.toLowerCase(Locale.ENGLISH);
    private static final int BEARER_PREFIX_LENGTH = LOWERCASE_BEARER_PREFIX.length();
    private static final String JWT = "jwt";

    @Override
    public Uni<SecurityIdentity> authenticate(RoutingContext context, IdentityProviderManager identityProviderManager) {
        var headers = context.request().headers().getAll(HttpHeaderNames.AUTHORIZATION);
        if (headers != null) {
            for (String header : headers) {
                if (header.toLowerCase(Locale.ENGLISH).startsWith(LOWERCASE_BEARER_PREFIX)) {
                    var token = header.substring(BEARER_PREFIX_LENGTH);
                    var request = new TokenAuthenticationRequest(new TokenCredential(token, JWT));
                    return identityProviderManager.authenticate(request);
                }
            }
        }

        return Uni.createFrom().optional(Optional.empty());
    }

    @Override
    public Uni<ChallengeData> getChallenge(RoutingContext context) {
        return Uni.createFrom().item(new ChallengeData(
                HttpResponseStatus.UNAUTHORIZED.code(),
                HttpHeaderNames.WWW_AUTHENTICATE,
                 BEARER));
    }

    @Override
    public Set<Class<? extends AuthenticationRequest>> getCredentialTypes() {
        return Collections.singleton(TokenAuthenticationRequest.class);
    }

    @Override
    public HttpCredentialTransport getCredentialTransport() {
        return new HttpCredentialTransport(HttpCredentialTransport.Type.AUTHORIZATION, BEARER);
    }
}
