package ru.bardinpetr.itmo.lab3.app.auth.jwt.keys.impl;


import jakarta.enterprise.context.ApplicationScoped;
import lombok.Data;
import ru.bardinpetr.itmo.lab3.app.auth.jwt.models.JWTType;

@ApplicationScoped
@Data
public class AppKeyStorage extends JWTHMACKeyProvider {
    public AppKeyStorage() {
        registerGenerate(JWTType.ACCESS.getKid());
        registerGenerate(JWTType.REFRESH.getKid());
    }
}
