package com.inkpot.api.iam;

import io.quarkus.security.credential.PasswordCredential;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.runtime.QuarkusPrincipal;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;

import java.util.Objects;
import java.util.UUID;

public final class User {

    private final UUID authorId;
    private final String username;
    private final String encryptedPassword;

    private User(Builder builder) {
        this.authorId = builder.authorId;
        this.username = builder.username;
        this.encryptedPassword = builder.encryptedPassword;
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

    public SecurityIdentity newSecurityIdentity() {
        return QuarkusSecurityIdentity.builder()
                .setPrincipal(new QuarkusPrincipal(authorId.toString()))
                .addAttribute(Field.AUTHOR_ID, authorId)
                .addAttribute(Field.USERNAME, username)
                .addAttribute(Field.ENCRYPTED_PASSWORD, encryptedPassword)
                .addCredential(new PasswordCredential(encryptedPassword.toCharArray()))
                .build();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAuthorId(), getUsername(), getEncryptedPassword());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(getAuthorId(), user.getAuthorId()) && Objects.equals(getUsername(), user.getUsername()) && Objects.equals(getEncryptedPassword(), user.getEncryptedPassword());
    }

    public Token generateToken() {
        return Token.builder().subject(username).build();
    }

    public static String recoverUsername(Token token) {
        return token.getSubject();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

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
            return new User(this);
        }

    }

    public static final class Field {

        public static final String USERNAME = "username";
        public static final String AUTHOR_ID = "authorId";
        public static final String ENCRYPTED_PASSWORD = "encryptedPassword";

    }

}
