package ru.bardinpetr.itmo.lab3.app.auth.db.utils;

import jakarta.enterprise.context.ApplicationScoped;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

@ApplicationScoped
public class PasswordService {
    private static final int SALT_SIZE = 16;
    private static final int KEY_SIZE = 128;
    private static final int ITERATIONS_COUNT = 10;

    private final SecureRandom random = new SecureRandom();
    private final SecretKeyFactory factory;

    public PasswordService() {
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not initialize crypto");
        }
    }

    public String encode(String password) {
        var salt = new byte[SALT_SIZE];
        random.nextBytes(salt);
        return hash(password, salt);
    }

    public boolean check(String hash, String input) {
        var src = Base64Helper.splitDecode(hash);
        if (src.size() != 2) return false;
        return hash(input, src.get(0)).equals(hash);
    }

    private String hash(String password, byte[] salt) {
        var keySpec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS_COUNT, KEY_SIZE);
        byte[] key;
        try {
            key = factory.generateSecret(keySpec).getEncoded();
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("Could not initialize crypto");
        }

        return Base64Helper.joinEncode(salt, key);
    }
}
