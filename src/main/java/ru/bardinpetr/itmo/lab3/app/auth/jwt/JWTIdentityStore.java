package ru.bardinpetr.itmo.lab3.app.auth.jwt;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;
import lombok.extern.slf4j.Slf4j;
import ru.bardinpetr.itmo.lab3.app.auth.db.models.DBUserPrincipal;
import ru.bardinpetr.itmo.lab3.app.auth.jwt.models.JWTPairCredential;
import ru.bardinpetr.itmo.lab3.app.auth.jwt.payload.JWTPayloadService;
import ru.bardinpetr.itmo.lab3.app.auth.jwt.token.JWTParsers;
import ru.bardinpetr.itmo.lab3.data.dao.impl.UserDAO;

import java.util.Set;

@ApplicationScoped
@Slf4j
public class JWTIdentityStore implements IdentityStore {

    @Inject
    private JWTPayloadService payloadService;
    @Inject
    private JWTParsers parserService;
    @Inject
    private UserDAO userDAO;


    @Override
    public Set<String> getCallerGroups(CredentialValidationResult validationResult) {
        return validationResult.getCallerGroups();
    }

    @Override
    public CredentialValidationResult validate(Credential credential) {
        if (credential instanceof JWTPairCredential token) {
            if (token.getAccessToken().isEmpty())
                return CredentialValidationResult.INVALID_RESULT;

            var auth = token.getAccessToken().get();
            try {
                var data = parserService
                        .forType(auth.getType())
                        .parseClaimsJws(auth.getToken());

                var principal = payloadService.extract(data);

                var dbPrincipal = userDAO.findByLogin(principal.getName());
                if (dbPrincipal.isEmpty())
                    return CredentialValidationResult.INVALID_RESULT;
                var user = dbPrincipal.get();

                return new CredentialValidationResult(
                        new DBUserPrincipal(user),
                        userDAO.getRoles(user)
                );
            } catch (Exception ex) {
                log.error("JWT validation error", ex);
                return CredentialValidationResult.INVALID_RESULT;
            }
        }
        return CredentialValidationResult.NOT_VALIDATED_RESULT;
    }
}
