package com.inkpot.api.iam;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import static com.inkpot.api.iam.EncryptionUtil.sha512;

@ApplicationScoped
public class Authenticator {

    private final UserDao userDao;

    @Inject
    public Authenticator(UserDao userDao) {
        this.userDao = userDao;
    }

    public User authenticate(String username, String password) throws AuthenticationException {
        var user = userDao.readUser(username).orElseThrow(this::noUserFoundException);

        if (user.getEncryptedPassword().equals(sha512(password))) {
            return user;
        }

        throw new AuthenticationException("Bad password");
    }

    public User authenticate(String stringToken) throws AuthenticationException {
        validateToken(stringToken);

        var token = Token.fromStringToken(stringToken);

        return userDao.readUser(User.recoverUsername(token)).orElseThrow(this::noUserFoundException);
    }

    private AuthenticationException noUserFoundException() {
        return new AuthenticationException("No user found");
    }

    private void validateToken(String token) throws AuthenticationException {
        if (!Token.isValidStringToken(token)) {
            throw new AuthenticationException("Invalid token");
        }
    }
}
