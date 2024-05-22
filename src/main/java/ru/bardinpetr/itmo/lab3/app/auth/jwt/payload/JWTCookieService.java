package ru.bardinpetr.itmo.lab3.app.auth.jwt.payload;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.bardinpetr.itmo.lab3.app.auth.jwt.models.JWTPairCredential;
import ru.bardinpetr.itmo.lab3.app.auth.jwt.models.JWTType;
import ru.bardinpetr.itmo.lab3.app.auth.jwt.models.JWTWrapper;
import ru.bardinpetr.itmo.lab3.utils.CookieBuilder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ru.bardinpetr.itmo.lab3.app.auth.jwt.models.JWTType.ACCESS;
import static ru.bardinpetr.itmo.lab3.app.auth.jwt.models.JWTType.REFRESH;
import static ru.bardinpetr.itmo.lab3.utils.RequestUtils.getCookies;
import static ru.bardinpetr.itmo.lab3.utils.RequestUtils.setCookies;

@ApplicationScoped
public class JWTCookieService {
    public static final String ACCESS_TOKEN_COOKIE = "wl3_token_access";
    public static final String REFRESH_TOKEN_COOKIE = "wl3_token_refresh";

    protected Optional<JWTWrapper> wrapTokenCookie(Map<String, Cookie> cookies, String name, JWTType type) {
        if (!cookies.containsKey(name))
            return Optional.empty();

        var value = cookies.get(name).getValue();
        if (value.isBlank())
            return Optional.empty();

        return Optional.of(new JWTWrapper(type, value));
    }

    public JWTPairCredential extract(HttpServletRequest request) {
        var cookies = getCookies(request);
        return new JWTPairCredential(
                wrapTokenCookie(cookies, ACCESS_TOKEN_COOKIE, ACCESS),
                wrapTokenCookie(cookies, REFRESH_TOKEN_COOKIE, REFRESH)
        );
    }

    public void clear(HttpServletResponse response) {
        setCookies(
                response,
                true,
                List.of(
                        CookieBuilder
                                .named(ACCESS_TOKEN_COOKIE)
                                .value("")
                                .maxAge(1),
                        CookieBuilder
                                .named(REFRESH_TOKEN_COOKIE)
                                .value("")
                                .maxAge(1)

                )
        );
    }

    public void inject(HttpServletResponse response, JWTPairCredential authData) {
        if (authData.getAccessToken().isEmpty() || authData.getRefreshToken().isEmpty())
            throw new RuntimeException("Sending cookies without one of tokens is prohibited");

        var access = authData.getAccessToken().get();
        var refresh = authData.getRefreshToken().get();

        setCookies(
                response,
                true,
                List.of(
                        CookieBuilder
                                .named(ACCESS_TOKEN_COOKIE)
                                .value(access.getToken())
                                .maxAge(access.getType().getExpiry()),
                        CookieBuilder
                                .named(REFRESH_TOKEN_COOKIE)
                                .value(refresh.getToken())
                                .maxAge(refresh.getType().getExpiry())
                )
        );
    }
}
