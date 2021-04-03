package com.inkpot.api.store;

import com.inkpot.api.iam.UserStore;
import com.inkpot.api.iam.User;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TinkerGraphUserStore implements UserStore {
    @Override
    public User readUser(String username) {
        return null;
    }
}
