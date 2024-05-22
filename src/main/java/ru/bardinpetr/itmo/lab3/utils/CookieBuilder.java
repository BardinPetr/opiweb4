package ru.bardinpetr.itmo.lab3.utils;

import jakarta.servlet.http.Cookie;

public class CookieBuilder {

    private Integer maxAge = null;
    private String name;
    private String value;
    private String domain;
    private String path;

    public static CookieBuilder named(String name) {
        var builder = new CookieBuilder();
        builder.name(name);
        return builder;
    }

    public CookieBuilder name(String name) {
        this.name = name;
        return this;
    }

    public CookieBuilder value(Object value) {
        this.value = String.valueOf(value);
        return this;
    }

    public CookieBuilder domain(String value) {
        this.domain = String.valueOf(value);
        return this;
    }

    public CookieBuilder path(String value) {
        this.path = value;
        return this;
    }

    public CookieBuilder maxAge(int seconds) {
        this.maxAge = seconds;
        return this;
    }

    public Cookie build() {
        var cookie = new Cookie(this.name, this.value);
        if (this.name == null || this.value == null)
            throw new RuntimeException("Not supplied name or value");

        if (this.domain != null)
            cookie.setDomain(this.domain);
        if (this.maxAge != null)
            cookie.setMaxAge(this.maxAge);
        if (this.path != null)
            cookie.setPath(this.path);

        return cookie;
    }

    public enum SameSite {
        STRICT, LAX, NONE;

        @Override
        public String toString() {
            return name().toUpperCase().charAt(0) + name().toLowerCase().substring(1);
        }
    }
}
