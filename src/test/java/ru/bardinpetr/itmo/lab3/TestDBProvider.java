package ru.bardinpetr.itmo.lab3;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Driver;
import java.util.HashMap;
import java.util.Map;

public class TestDBProvider {
    private final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withReuse(true);

    @Getter
    private final EntityManager entityManager;
    @Getter
    private final CriteriaBuilder criteriaBuiler;
    @Getter
    private final EntityManagerFactory entityManagerFactory;


    public TestDBProvider() {
        postgres.start();

        entityManagerFactory = createEntityManagerFactory();
        entityManager = entityManagerFactory.createEntityManager();
        criteriaBuiler = entityManager.getCriteriaBuilder();
    }

    public void close() {
        entityManager.close();
        entityManagerFactory.close();
        postgres.close();
    }

    private Map<Object, Object> getJPAProperties() {
        var properties = new HashMap<>();
        properties.put("jakarta.persistence.jdbc.url", postgres.getJdbcUrl());
        properties.put("jakarta.persistence.jdbc.user", postgres.getUsername());
        properties.put("jakarta.persistence.jdbc.password", postgres.getPassword());
        properties.put("jakarta.persistence.jdbc.driver", postgres.getDriverClassName());
        return properties;
    }

    private EntityManagerFactory createEntityManagerFactory() {
        return Persistence
                .createEntityManagerFactory(
                        "test",
                        getJPAProperties()
                );
    }
}
