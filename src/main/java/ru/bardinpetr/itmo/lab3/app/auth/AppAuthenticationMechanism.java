package ru.bardinpetr.itmo.lab3.app.auth;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.bardinpetr.itmo.lab3.app.auth.jwt.models.JWTPairCredential;
import ru.bardinpetr.itmo.lab3.app.auth.jwt.models.JWTType;
import ru.bardinpetr.itmo.lab3.app.auth.jwt.payload.JWTCallerPrincipal;
import ru.bardinpetr.itmo.lab3.app.auth.jwt.payload.JWTCookieService;
import ru.bardinpetr.itmo.lab3.app.auth.jwt.token.JWTIssueService;

import java.util.Optional;

@ApplicationScoped
@Slf4j
public class AppAuthenticationMechanism implements HttpAuthenticationMechanism {

    public static final String LOGIN_PAGE = "/views/login.xhtml";
    public static final String START_PAGE = "/views/home.xhtml";

    @Inject
    private IdentityStoreHandler identityStoreHandler;
    @Inject
    private JWTCookieService cookieService;
    @Inject
    private JWTIssueService jwtIssueService;


    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest req, HttpServletResponse res, HttpMessageContext ctx) {
        var upCred = getLoginCredentials(req, res, ctx);
        var jwtCred = getJWTCredentials(req, res, ctx);

        if (upCred.isPresent()) {
            log.info("Auth by user/pass for {}", req.getServletPath());

            var valid = identityStoreHandler.validate(upCred.get());
            if (valid.getStatus() != CredentialValidationResult.Status.VALID)
                return ctx.doNothing();

            issueToken(res, valid);
            return ctx.notifyContainerAboutLogin(valid);
        }

        if (jwtCred.isPresent()) {
            log.debug("Auth by JWT for {}", req.getServletPath());

            var valid = identityStoreHandler.validate(jwtCred.get());
            if (valid.getStatus() != CredentialValidationResult.Status.VALID)
                return redirectToLogin(req, res, ctx);

            return ctx.notifyContainerAboutLogin(valid);
        }

        if (ctx.getCallerPrincipal() != null)
            return ctx.doNothing();

        if (ctx.isProtected())
            return redirectToLogin(req, res, ctx);

        return ctx.doNothing();
    }

    /**
     * Create and send token with response for principal from validation result
     */
    private void issueToken(HttpServletResponse response, CredentialValidationResult result) {
        if (result.getStatus() != CredentialValidationResult.Status.VALID)
            return;

        var jwtPrincipal = new JWTCallerPrincipal(
                result.getCallerPrincipal().getName(),
                result.getCallerGroups(),
                JWTType.ACCESS
        );
        log.info("Issued token for {}", jwtPrincipal.getName());
        cookieService.inject(response, jwtIssueService.issue(jwtPrincipal));
    }

    private AuthenticationStatus redirectToLogin(HttpServletRequest req, HttpServletResponse res, HttpMessageContext ctx) {
        log.info("Sent redirect to login");
        cleanSubject(req, res, ctx);
        return ctx.redirect("/app" + LOGIN_PAGE);
    }

    private AuthenticationStatus responseUnauthorized(HttpServletRequest req, HttpServletResponse res, HttpMessageContext ctx) {
        log.info("Sent unauthorized response to {}", req.getRequestURL());
        cleanSubject(req, res, ctx);
        return ctx.responseUnauthorized();
    }

    @Override
    public void cleanSubject(HttpServletRequest req, HttpServletResponse res, HttpMessageContext ctx) {
        log.info("Clearing session {}", req.getUserPrincipal());
        cookieService.clear(res);
        HttpAuthenticationMechanism.super.cleanSubject(req, res, ctx);
    }

    /**
     * Try to load username/password credentials from LoginBean that should put it into auth parameters
     */
    private Optional<UsernamePasswordCredential> getLoginCredentials(HttpServletRequest req, HttpServletResponse res, HttpMessageContext ctx) {
        var auth = ctx.getAuthParameters();
        if (auth.isNewAuthentication() &&
                auth.getCredential() instanceof UsernamePasswordCredential cred)
            return Optional.of(cred);
        return Optional.empty();
    }

    /**
     * Extract jwt credentials from cookie
     */
    private Optional<JWTPairCredential> getJWTCredentials(HttpServletRequest req, HttpServletResponse res, HttpMessageContext ctx) {
        var cred = cookieService.extract(req);
        return cred.isValid() ? Optional.of(cred) : Optional.empty();
    }
}
