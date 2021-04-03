package com.inkpot.api.dao;

import com.inkpot.api.iam.UserDao;
import com.inkpot.api.iam.User;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TinkerGraphUserDao implements UserDao {
    @Override
    public User readUser(String username) {
        return null;
    }
}
