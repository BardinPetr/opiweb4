package ru.bardinpetr.itmo.lab3.app.auth.jwt.token;

import io.jsonwebtoken.Jwts;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ru.bardinpetr.itmo.lab3.app.auth.jwt.keys.JWTKeyProvider;
import ru.bardinpetr.itmo.lab3.app.auth.jwt.keys.impl.AppKeyStorage;
import ru.bardinpetr.itmo.lab3.app.auth.jwt.models.JWTPairCredential;
import ru.bardinpetr.itmo.lab3.app.auth.jwt.models.JWTType;
import ru.bardinpetr.itmo.lab3.app.auth.jwt.models.JWTWrapper;
import ru.bardinpetr.itmo.lab3.app.auth.jwt.payload.JWTCallerPrincipal;
import ru.bardinpetr.itmo.lab3.app.auth.jwt.payload.JWTPayloadService;

import java.util.*;

import static ru.bardinpetr.itmo.lab3.app.auth.jwt.payload.JWTPayloadService.CLAIM_KID;

@ApplicationScoped
public class JWTIssueService {
    public static final String ISSUER = "lab3-server";

    @Inject
    private AppKeyStorage keyProvider;
    @Inject
    private JWTPayloadService payloadService;


    public JWTPairCredential issue(JWTCallerPrincipal subject) {
        return new JWTPairCredential(
                createToken(JWTType.ACCESS, subject),
                createToken(JWTType.REFRESH, subject)
        );
    }

    protected Optional<JWTWrapper> createToken(JWTType type, JWTCallerPrincipal subject) {
        var exp = Calendar.getInstance();
        exp.add(Calendar.SECOND, type.getExpiry());
        var now = new Date();

        var token =
                payloadService.inject(
                                Jwts.builder(),
                                subject
                        )
                        .setHeader(Map.of(CLAIM_KID, type.getKid()))
                        .claim(CLAIM_KID, type.getKid())
                        .setExpiration(exp.getTime())
                        .setIssuedAt(now)
                        .setNotBefore(now)
                        .setId(UUID.randomUUID().toString())
                        .setIssuer(ISSUER)
                        .setAudience(type.getAud())
                        .signWith(keyProvider.resolveSigningKey(type.getKid()))
                        .compact();

        return Optional.of(new JWTWrapper(type, token));
    }
}
