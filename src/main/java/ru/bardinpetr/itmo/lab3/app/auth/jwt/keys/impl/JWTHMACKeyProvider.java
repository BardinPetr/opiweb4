package ru.bardinpetr.itmo.lab3.app.auth.jwt.keys.impl;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import ru.bardinpetr.itmo.lab3.app.auth.jwt.keys.JWTKeyProvider;

public class JWTHMACKeyProvider extends JWTKeyProvider {
    @Override
    public void registerGenerate(String kid) {
        register(kid, Keys.secretKeyFor(SignatureAlgorithm.HS512));
    }
}
