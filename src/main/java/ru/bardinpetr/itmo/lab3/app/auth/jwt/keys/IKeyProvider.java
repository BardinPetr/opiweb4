package ru.bardinpetr.itmo.lab3.app.auth.jwt.keys;

import java.security.Key;

public interface IKeyProvider {
    void register(String kid, Key key);

    void registerGenerate(String kid);

    Key resolveSigningKey(String kid);
}
