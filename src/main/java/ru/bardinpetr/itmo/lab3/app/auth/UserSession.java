package ru.bardinpetr.itmo.lab3.app.auth;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.SecurityContext;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import ru.bardinpetr.itmo.lab3.app.auth.db.models.DBUserPrincipal;
import ru.bardinpetr.itmo.lab3.context.ContextProvider;
import ru.bardinpetr.itmo.lab3.data.models.User;
import ru.bardinpetr.itmo.lab3.navigation.NavigationController;

import java.io.Serializable;

@Named
@SessionScoped
@Slf4j
public class UserSession implements Serializable {

    @Inject
    private ExternalContext externalContext;
    @Inject
    private SecurityContext securityContext;
    @Inject
    private ContextProvider contextProvider;
    @Inject
    private NavigationController navigation;


    public boolean isLoggedIn() {
        return getPrincipal() != null;
    }

    public DBUserPrincipal getPrincipal() {
        if (securityContext.getCallerPrincipal() instanceof DBUserPrincipal dbUser)
            return dbUser;
        return null;
    }

    public User getUser() {
        if (!isLoggedIn()) return null;
        return getPrincipal().getUser();
    }

    public String doLogout() {
        if (!isLoggedIn()) return null;
        log.info("User {} logout", getPrincipal().getName());
        try {
            contextProvider.getRequest().logout();
            externalContext.invalidateSession();
        } catch (ServletException e) {
            log.error("Error occurred during logout", e);
        }
        return navigation.toLogin();
    }
}
