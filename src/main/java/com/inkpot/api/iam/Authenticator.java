package com.inkpot.api.iam;

import io.quarkus.security.identity.CurrentIdentityAssociation;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import static com.inkpot.api.iam.EncryptionUtil.sha256;

@ApplicationScoped
public class Authenticator {

    private final UserDao userDao;
    private final CurrentIdentityAssociation currentIdentityAssociation;

    @Inject
    public Authenticator(UserDao userDao,
                         CurrentIdentityAssociation currentIdentityAssociation) {
        this.userDao = userDao;
        this.currentIdentityAssociation = currentIdentityAssociation;
    }

    public User authenticate(String username, String password) throws AuthenticationException {
        var user = userDao.readUser(username).orElseThrow(this::noUserFoundException);

        if (user.getEncryptedPassword().equals(sha256(password))) {
            return user;
        }

        throw new AuthenticationException("Bad password");
    }

    public User authenticate(String stringToken) throws AuthenticationException {
        validateToken(stringToken);

        var token = Token.fromStringToken(stringToken);

        return userDao.readUser(User.readUsernameFrom(token)).orElseThrow(this::noUserFoundException);
    }

    private AuthenticationException noUserFoundException() {
        return new AuthenticationException("No user found");
    }

    private void validateToken(String token) throws AuthenticationException {
        if (!Token.isValidStringToken(token)) {
            throw new AuthenticationException("Invalid token");
        }
    }


    public User currentAuthenticatedUser() {
        var username = User.readUsernameFrom(currentIdentityAssociation.getIdentity());
        return userDao.readUser(username).orElseThrow();
    }
}
