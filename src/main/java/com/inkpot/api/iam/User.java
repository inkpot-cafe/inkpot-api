package com.inkpot.api.iam;

import io.quarkus.security.credential.PasswordCredential;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.runtime.QuarkusPrincipal;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;

import java.util.UUID;

public class User {

    public static final String USERNAME = "username";
    public static final String AUTHOR_ID = "authorId";
    public static final String ENCRYPTED_PASSWORD = "encryptedPassword";

    private final UUID authorId;
    private final String username;
    private final String encryptedPassword;

    private User(UUID authorId, String username, String encryptedPassword) {
        this.authorId = authorId;
        this.username = username;
        this.encryptedPassword = encryptedPassword;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public String getUsername() {
        return username;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public SecurityIdentity toSecurityIdentity() {
        return QuarkusSecurityIdentity.builder()
                .setPrincipal(new QuarkusPrincipal(authorId.toString()))
                .addAttribute(AUTHOR_ID, authorId)
                .addAttribute(USERNAME, username)
                .addAttribute(ENCRYPTED_PASSWORD, encryptedPassword)
                .addCredential(new PasswordCredential(encryptedPassword.toCharArray()))
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {

        private UUID authorId;
        private String username;
        private String encryptedPassword;

        private Builder() {}

        public Builder authorId(UUID authorId) {
            this.authorId = authorId;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder encryptedPassword(String encryptedPassword) {
            this.encryptedPassword = encryptedPassword;
            return this;
        }

        public User build() {
            return new User(authorId, username, encryptedPassword);
        }

    }

}
