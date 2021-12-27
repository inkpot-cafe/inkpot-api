package com.inkpot.api.iam;

import java.util.Optional;

public interface UserDao {
    Optional<User> readUser(String username);
}
