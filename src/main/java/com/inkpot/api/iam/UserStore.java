package com.inkpot.api.iam;

public interface UserStore {
    User readUser(String username);
}
