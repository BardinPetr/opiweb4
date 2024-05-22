package ru.bardinpetr.itmo.lab3.data.dao.impl;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.bardinpetr.itmo.lab3.data.dao.DAO;
import ru.bardinpetr.itmo.lab3.data.models.PointResult;
import ru.bardinpetr.itmo.lab3.data.models.Role;
import ru.bardinpetr.itmo.lab3.data.models.User;
import ru.bardinpetr.itmo.lab3.data.util.EntityManagerProvider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Named("userDAO")
@ApplicationScoped
@Slf4j
public class UserDAO extends DAO<Long, User> implements Serializable {

    @Inject
    private EntityManagerProvider entityManagerProvider;

    @Inject
    private RoleDAO roleDAO;


    public UserDAO() {
        super(User.class);
    }

    @PostConstruct
    public void init() {
        setManager(entityManagerProvider.getEntityManager());
    }

    public Optional<User> findByLogin(String login) {
        var res = findMatching("login", login);
        return res.isEmpty() ? Optional.empty() : Optional.of(res.get(0));
    }

    public List<PointResult> getPointResults(User user) {
        return fetch(user.getId(), List.of("pointResults"))
                .map(User::getPointResults)
                .orElse(new ArrayList<>());
    }

    public Set<String> getRoles(User user) {
        return fetch(user.getId(), List.of("roles"))
                .map(User::getRoles)
                .orElse(Set.of())
                .stream()
                .map(Role::getValue)
                .collect(Collectors.toSet());
    }

    public void addRole(User user, String roleName) {
        user.getRoles().add(roleDAO.instance(roleName));
        update(user);
    }

    public void addPointResult(User user, PointResult result) {
        user.getPointResults().add(result);
        update(user);
    }
}
