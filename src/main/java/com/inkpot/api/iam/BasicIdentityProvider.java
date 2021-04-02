package com.inkpot.api.iam;

import io.quarkus.security.credential.Credential;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.IdentityProvider;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.UsernamePasswordAuthenticationRequest;
import io.smallrye.mutiny.Uni;

import javax.inject.Singleton;
import java.security.Permission;
import java.security.Principal;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Singleton
public class BasicIdentityProvider implements IdentityProvider<UsernamePasswordAuthenticationRequest> {

    @Override
    public Class<UsernamePasswordAuthenticationRequest> getRequestType() {
        return UsernamePasswordAuthenticationRequest.class;
    }


    private final SecurityIdentity securityIdentity = new SecurityIdentity() {
        @Override
        public Principal getPrincipal() {
            return () -> "";
        }

        @Override
        public boolean isAnonymous() {
            return false;
        }

        @Override
        public Set<String> getRoles() {
            return Set.of(Role.AUTHOR);
        }

        @Override
        public boolean hasRole(String role) {
            return true;
        }

        @Override
        public <T extends Credential> T getCredential(Class<T> credentialType) {
            return null;
        }

        @Override
        public Set<Credential> getCredentials() {
            return null;
        }

        @Override
        public <T> T getAttribute(String name) {
            return null;
        }

        @Override
        public Map<String, Object> getAttributes() {
            return Collections.emptyMap();
        }

        @Override
        public Uni<Boolean> checkPermission(Permission permission) {
            return Uni.createFrom().item(false);
        }
    };

    @Override
    public Uni<SecurityIdentity> authenticate(UsernamePasswordAuthenticationRequest request, AuthenticationRequestContext context) {
        return Uni.createFrom().item(securityIdentity);
    }

}
