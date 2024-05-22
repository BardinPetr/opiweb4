package ru.bardinpetr.itmo.lab3.app.auth.jwt.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum JWTType {
    ACCESS(60 * 60, "kid-access", "app"),
    REFRESH(60 * 60, "kid-refresh", "app-auth");

    private final int expiry;
    private final String kid;
    private final String aud;

    public static JWTType byKid(String kid) {
        return Arrays
                .stream(JWTType.values())
                .filter(i -> i.kid.equals(kid))
                .findFirst()
                .orElseThrow();
    }
}