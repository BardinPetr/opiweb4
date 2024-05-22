package ru.bardinpetr.itmo.lab3.app.auth.db.models;

import jakarta.security.enterprise.CallerPrincipal;
import lombok.Data;
import ru.bardinpetr.itmo.lab3.data.models.User;

@Data
public class DBUserPrincipal extends CallerPrincipal {
    private final User user;

    public DBUserPrincipal(User user) {
        super(user.getLogin());
        this.user = user;
    }
}
