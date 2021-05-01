package com.inkpot.api.iam.quarkus;

import com.inkpot.api.iam.AuthenticationException;
import com.inkpot.api.iam.Authenticator;
import io.quarkus.security.AuthenticationFailedException;
import io.quarkus.security.credential.TokenCredential;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.IdentityProvider;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.TokenAuthenticationRequest;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class TokenIdentityProvider implements IdentityProvider<TokenAuthenticationRequest> {

    private final Authenticator authenticator;

    @Inject
    public TokenIdentityProvider(Authenticator authenticator) {
        this.authenticator = authenticator;
    }

    @Override
    public Class<TokenAuthenticationRequest> getRequestType() {
        return TokenAuthenticationRequest.class;
    }

    @Override
    public Uni<SecurityIdentity> authenticate(TokenAuthenticationRequest request, AuthenticationRequestContext context) {
        String stringToken = readStringToken(request.getToken());;

        try {
            var user = authenticator.authenticate(stringToken);
            return Uni.createFrom().item(user.toSecurityIdentity());
        } catch (AuthenticationException e) {
            throw new AuthenticationFailedException(e);
        }
    }

    private String readStringToken(TokenCredential token) {
        return token.getToken();
    }
}
