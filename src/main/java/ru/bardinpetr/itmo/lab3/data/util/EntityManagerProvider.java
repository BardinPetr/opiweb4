package ru.bardinpetr.itmo.lab3.data.util;


import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Named("entityManagerProvider")
@ApplicationScoped
@Data
@Slf4j
public class EntityManagerProvider implements Serializable {
    private EntityManager entityManager;
    private CriteriaBuilder criteriaBuilder;

    @PostConstruct
    public void init() {
        entityManager = Persistence.createEntityManagerFactory("lab3").createEntityManager();
        criteriaBuilder = entityManager.getCriteriaBuilder();
        log.info("Initialized entity manager provider");
    }
}
