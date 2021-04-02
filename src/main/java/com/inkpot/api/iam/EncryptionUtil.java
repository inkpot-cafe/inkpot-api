package com.inkpot.api.iam;

import org.eclipse.microprofile.config.ConfigProvider;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptionUtil {

    private static final String ENCRYPTION_PASSWORD = "encryption.password";
    private static final String SHA_512 = "SHA-512";

    public static String sha512(String str) {
        var md = messageDigest();
        addSalt(md);
        return encrypt(str, md);
    }

    private static MessageDigest messageDigest() {
        try {
            return MessageDigest.getInstance(SHA_512);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void addSalt(MessageDigest md) {
        var encryptionPassword = ConfigProvider.getConfig().getValue(ENCRYPTION_PASSWORD, String.class);
        md.update(encryptionPassword.getBytes(StandardCharsets.UTF_8));
    }

    private static String encrypt(String str, MessageDigest md) {
        return new String(md.digest(str.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }
}
