package ru.bardinpetr.itmo.lab3.app.auth.jwt.payload;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import ru.bardinpetr.itmo.lab3.app.auth.jwt.error.JWTPayloadException;
import ru.bardinpetr.itmo.lab3.app.auth.jwt.models.JWTType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class JWTPayloadService {
    public static final String CLAIM_KID = "kid";
    public static final String CLAIM_ROLE = "roles";

    public JwtBuilder inject(JwtBuilder builder, JWTCallerPrincipal user) {
        return builder
                .setSubject(user.getName())
                .claim(CLAIM_ROLE, user.getGroups());
    }

    public JWTCallerPrincipal extract(Jws<Claims> token) throws JWTPayloadException {
        return new JWTCallerPrincipal(
                token.getBody().getSubject(),
                extractGroups(token),
                extractType(token)
        );
    }

    private JWTType extractType(Jws<Claims> token) throws JWTPayloadException {
        try {
            return JWTType.byKid((String) token.getHeader().get(CLAIM_KID));
        } catch (IllegalArgumentException ex) {
            throw new JWTPayloadException();
        }
    }

    @SuppressWarnings("unchecked")
    private Set<String> extractGroups(Jws<Claims> claims) throws JWTPayloadException {
        try {
            return new HashSet<String>(claims.getBody().get(CLAIM_ROLE, ArrayList.class));
        } catch (Exception ex) {
            throw new JWTPayloadException();
        }
    }
}
