package ru.bardinpetr.itmo.lab3.app.auth;


import jakarta.annotation.security.DeclareRoles;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@DeclareRoles({"user", "admin", "anon"})
public class AppConfig {
}
