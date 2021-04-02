package com.inkpot.api.iam;

public interface AuthStore {
    User readUser(String username);
}
