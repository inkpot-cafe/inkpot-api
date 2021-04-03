package com.inkpot.api.iam.quarkus;

import com.inkpot.api.iam.AuthenticationException;
import com.inkpot.api.iam.Authenticator;
import io.quarkus.security.AuthenticationFailedException;
import io.quarkus.security.credential.PasswordCredential;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.IdentityProvider;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.UsernamePasswordAuthenticationRequest;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class BasicIdentityProvider implements IdentityProvider<UsernamePasswordAuthenticationRequest> {

    private final Authenticator authenticator;

    @Inject
    public BasicIdentityProvider(Authenticator authenticator) {
        this.authenticator = authenticator;
    }

    @Override
    public Class<UsernamePasswordAuthenticationRequest> getRequestType() {
        return UsernamePasswordAuthenticationRequest.class;
    }

    @Override
    public Uni<SecurityIdentity> authenticate(UsernamePasswordAuthenticationRequest request, AuthenticationRequestContext context) {
        try {
            var user = authenticator.authenticate(request.getUsername(), stringify(request.getPassword()));
            return Uni.createFrom().item(user.toSecurityIdentity());
        } catch (AuthenticationException e) {
            e.printStackTrace();
            throw new AuthenticationFailedException(e);
        }
    }

    private String stringify(PasswordCredential request) {
        return String.valueOf(request.getPassword());
    }

}
