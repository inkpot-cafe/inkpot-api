package com.inkpot.api.iam;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import static com.inkpot.api.iam.EncryptionUtil.sha512;

@ApplicationScoped
public class Authenticator {

    private final AuthStore authStore;

    @Inject
    public Authenticator(AuthStore authStore) {
        this.authStore = authStore;
    }

    public User authenticate(String username, String password) throws AuthenticationException {
        var user = authStore.readUser(username);

        if (user.getEncryptedPassword().equals(sha512(password))) {
            return user;
        }

        throw new AuthenticationException("Bad password");
    }

    public User authenticate(String stringToken) throws AuthenticationException {
        validateToken(stringToken);

        var token = Token.fromStringToken(stringToken);

        return authStore.readUser(User.recoverUsername(token));
    }

    private void validateToken(String token) throws AuthenticationException {
        if (!Token.isValidStringToken(token)) {
            throw new AuthenticationException("Invalid token");
        }
    }
}
