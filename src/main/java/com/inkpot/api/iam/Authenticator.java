package com.inkpot.api.iam;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import static com.inkpot.api.iam.EncryptionUtil.sha512;

@ApplicationScoped
public class Authenticator {

    private final UserStore userStore;

    @Inject
    public Authenticator(UserStore userStore) {
        this.userStore = userStore;
    }

    public User authenticate(String username, String password) throws AuthenticationException {
        var user = userStore.readUser(username);

        if (user.getEncryptedPassword().equals(sha512(password))) {
            return user;
        }

        throw new AuthenticationException("Bad password");
    }

    public User authenticate(String stringToken) throws AuthenticationException {
        validateToken(stringToken);

        var token = Token.fromStringToken(stringToken);

        return userStore.readUser(User.recoverUsername(token));
    }

    private void validateToken(String token) throws AuthenticationException {
        if (!Token.isValidStringToken(token)) {
            throw new AuthenticationException("Invalid token");
        }
    }
}
