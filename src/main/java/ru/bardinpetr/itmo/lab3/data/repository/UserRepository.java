package ru.bardinpetr.itmo.lab3.data.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Data;
import ru.bardinpetr.itmo.lab3.data.dao.impl.UserDAO;
import ru.bardinpetr.itmo.lab3.data.models.Point;
import ru.bardinpetr.itmo.lab3.data.models.PointResult;
import ru.bardinpetr.itmo.lab3.data.models.User;
import ru.bardinpetr.itmo.lab3.data.models.AreaConfig;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Named("userRepo")
@ApplicationScoped
public class UserRepository implements Serializable {
    @Inject
    private UserDAO userDAO;
}
