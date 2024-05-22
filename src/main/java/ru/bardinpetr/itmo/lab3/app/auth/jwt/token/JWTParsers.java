package ru.bardinpetr.itmo.lab3.app.auth.jwt.token;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ru.bardinpetr.itmo.lab3.app.auth.jwt.keys.impl.AppKeyStorage;
import ru.bardinpetr.itmo.lab3.app.auth.jwt.models.JWTType;

import static ru.bardinpetr.itmo.lab3.app.auth.jwt.token.JWTIssueService.ISSUER;
import static ru.bardinpetr.itmo.lab3.app.auth.jwt.payload.JWTPayloadService.CLAIM_KID;

@ApplicationScoped
public class JWTParsers {
    @Inject
    private AppKeyStorage keyProvider;

    private JwtParser refreshDecoder;
    private JwtParser accessDecoder;

    @PostConstruct
    private void init() {
        accessDecoder = createParser(JWTType.ACCESS);
        refreshDecoder = createParser(JWTType.REFRESH);
    }

    private JwtParser createParser(JWTType type) {
        return Jwts.parserBuilder()
                .setSigningKeyResolver(keyProvider.getDecodeKeyResolver())
                .setAllowedClockSkewSeconds(1)
                .requireIssuer(ISSUER)
                .requireAudience(type.getAud())
                .require(CLAIM_KID, type.getKid())
                .build();
    }

    public JwtParser forType(JWTType type) {
        return type == JWTType.ACCESS ? accessDecoder : refreshDecoder;
    }
}
