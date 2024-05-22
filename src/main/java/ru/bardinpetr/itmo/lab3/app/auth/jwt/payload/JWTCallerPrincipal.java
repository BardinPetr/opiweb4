package ru.bardinpetr.itmo.lab3.app.auth.jwt.payload;

import jakarta.security.enterprise.CallerPrincipal;
import lombok.Getter;
import ru.bardinpetr.itmo.lab3.app.auth.jwt.models.JWTType;

import java.util.Set;


public class JWTCallerPrincipal extends CallerPrincipal {
    @Getter
    private final Set<String> groups;
    @Getter
    private final JWTType type;

    public JWTCallerPrincipal(String name, Set<String> groups, JWTType tokenType) {
        super(name);
        this.groups = groups;
        this.type = tokenType;
    }
}
