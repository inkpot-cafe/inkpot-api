package com.inkpot.api.iam;

import io.quarkus.security.identity.SecurityIdentity;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.inkpot.api.iam.EncryptionUtil.sha512;

@Singleton
public class Authenticator {

    private final AuthStore authStore;

    @Inject
    public Authenticator(AuthStore authStore) {
        this.authStore = authStore;
    }

    public SecurityIdentity authenticate(String username, String password) throws AuthenticationException {
        var user = authStore.readUser(username);

        if (user.getEncryptedPassword().equals(sha512(password))) {
            return user.toSecurityIdentity();
        }

        throw new AuthenticationException("Bad password");
    }

}
