package ru.bardinpetr.itmo.lab3.app.auth.jwt.keys;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SigningKeyResolver;
import io.jsonwebtoken.UnsupportedJwtException;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

public abstract class JWTKeyProvider implements IKeyProvider {
    protected final Map<String, Key> keyMap = new HashMap<>();

    public void register(String kid, Key key) {
        keyMap.put(kid, key);
    }

    @Override
    public void registerGenerate(String kid) {
    }

    public Key resolveSigningKey(String kid) {
        return keyMap.get(kid);
    }

    public SigningKeyResolver getDecodeKeyResolver() {
        return new SigningKeyResolver() {
            @Override
            public Key resolveSigningKey(JwsHeader header, Claims claims) {
                try {
                    return keyMap.get(header.getKeyId());
                } catch (Exception ignored) {
                    return null;
                }
            }

            @Override
            public Key resolveSigningKey(JwsHeader header, String plaintext) {
                throw new UnsupportedJwtException("No key selection is allowed in plaintext");
            }
        };
    }
}

