package com.inkpot.api.store;

import com.inkpot.api.iam.AuthStore;
import com.inkpot.api.iam.User;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TinkerGraphAuthStore implements AuthStore {
    @Override
    public User readUser(String username) {
        return null;
    }
}
