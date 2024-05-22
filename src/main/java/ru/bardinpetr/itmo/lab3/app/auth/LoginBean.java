package ru.bardinpetr.itmo.lab3.app.auth;


import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.credential.Password;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.bardinpetr.itmo.lab3.app.auth.db.utils.PasswordService;
import ru.bardinpetr.itmo.lab3.context.ContextProvider;
import ru.bardinpetr.itmo.lab3.data.dao.impl.UserDAO;
import ru.bardinpetr.itmo.lab3.data.models.User;
import ru.bardinpetr.itmo.lab3.navigation.NavigationController;

import static jakarta.security.enterprise.AuthenticationStatus.SEND_FAILURE;
import static jakarta.security.enterprise.AuthenticationStatus.SUCCESS;
import static jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters.withParams;

@Data
@RequestScoped
@Named("loginBean")
@Slf4j
public class LoginBean {
    @Inject
    private NavigationController navigation;
    @Inject
    private ContextProvider contextReq;
    @Inject
    private UserDAO userDAO;
    @Inject
    private PasswordService passwordService;
    @Inject
    private Validator validator;
    @Inject
    private UserSession session;

    @NotNull(message = "Login should be provided")
    @Size(min = 3, message = "Login should be at last 3 characters")
    private String username;
    @NotNull(message = "Password should be provided")
    @Size(min = 8, message = "Password should be at last 8 characters")
    private String password;

    public String doLogin() {
        checkLoggedInUser();

        log.info("Started login authentication for {}", username);

        var cred = asCredential();
        if (cred == null) {
            log.error("User {} authentication failed", username);
            return null;
        }

        var status = contextReq.getSecurityContext().authenticate(
                contextReq.getRequest(),
                contextReq.getResponse(),
                withParams().newAuthentication(true).credential(cred)
        );
        log.info("User {} login status {}", username, status);

        if (status.equals(SUCCESS)) {
            contextReq.getContext().responseComplete();
            return navigation.toHome();
        } else if (status.equals(SEND_FAILURE)) {
            contextReq.sendMessage(FacesMessage.SEVERITY_ERROR, "Invalid username/password");
            log.error("User {} authentication failed", username);
        }
        return null;
    }

    public String doRegister() {
        checkLoggedInUser();

        log.info("Registering user {}", username);

        if (userDAO.findByLogin(username).isPresent()) {
            log.warn("Tried to register existing user {}", username);
            return null;
        }

        var hash = passwordService.encode(password);

        var user = new User();
        user.setLogin(username);
        user.setPasswordHash(hash);

        if (userDAO.insert(user)) {
            userDAO.addRole(user, "user");
            log.info("Registered user {} successfully", username);
        } else {
            log.warn("Register user {} failed", username);
        }

        return doLogin();
    }

    private void checkLoggedInUser() {
        session.doLogout();
    }

    protected UsernamePasswordCredential asCredential() {
        return validator.validate(this).isEmpty() ?
                new UsernamePasswordCredential(username, new Password(password)) : null;
    }
}
