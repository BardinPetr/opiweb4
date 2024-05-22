package ru.bardinpetr.itmo.lab3.app.auth.db;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;
import lombok.extern.slf4j.Slf4j;
import ru.bardinpetr.itmo.lab3.app.auth.db.models.DBUserPrincipal;
import ru.bardinpetr.itmo.lab3.app.auth.db.utils.PasswordService;
import ru.bardinpetr.itmo.lab3.data.dao.impl.UserDAO;

import java.util.Set;


@ApplicationScoped
@Slf4j
public class DBIdentityStore implements IdentityStore {

    @Inject
    private PasswordService passwordService;
    @Inject
    private UserDAO dao;


    @Override
    public CredentialValidationResult validate(Credential inputCredential) {
        if (inputCredential instanceof UsernamePasswordCredential cred) {
            log.debug("Validating {} over DB users", cred.getCaller());

            var username = cred.getCaller();

            var srcUser = dao.findByLogin(username);
            if (srcUser.isEmpty())
                return CredentialValidationResult.INVALID_RESULT;

            var user = srcUser.get();
            if (!passwordService.check(user.getPasswordHash(), cred.getPasswordAsString()))
                return CredentialValidationResult.INVALID_RESULT;

            return new CredentialValidationResult(
                    new DBUserPrincipal(user),
                    dao.getRoles(user)
            );
        }
        return CredentialValidationResult.NOT_VALIDATED_RESULT;
    }

    @Override
    public Set<String> getCallerGroups(CredentialValidationResult validationResult) {
        return validationResult.getCallerGroups();
    }
}