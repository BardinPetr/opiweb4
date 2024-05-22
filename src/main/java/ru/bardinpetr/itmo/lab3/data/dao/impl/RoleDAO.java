package ru.bardinpetr.itmo.lab3.data.dao.impl;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.bardinpetr.itmo.lab3.data.util.EntityManagerProvider;
import ru.bardinpetr.itmo.lab3.data.dao.DAO;
import ru.bardinpetr.itmo.lab3.data.models.Role;

import java.io.Serializable;

@Data
@Named("roleDAO")
@ApplicationScoped
@Slf4j
public class RoleDAO extends DAO<String, Role> implements Serializable {

    @Inject
    private EntityManagerProvider entityManagerProvider;

    public RoleDAO() {
        super(Role.class);
    }

    @PostConstruct
    public void init() {
        setManager(entityManagerProvider.getEntityManager());
    }

    public Role instance(String value) {
        return find(value)
                .orElseGet(() -> {
                    var newRole = Role.of(value);
                    insert(newRole);
                    return newRole;
                });
    }
}
