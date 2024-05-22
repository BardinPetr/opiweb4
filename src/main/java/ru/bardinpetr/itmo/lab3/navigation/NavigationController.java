package ru.bardinpetr.itmo.lab3.navigation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@Named("navigation")
@ApplicationScoped
public class NavigationController implements Serializable {

    public String toHome() {
        return "home";
    }

    public String toPoints() {
        return "points";
    }

    public String toLogin() {
        return "login";
    }
}
