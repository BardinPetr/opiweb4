package ru.bardinpetr.itmo.lab3.data.repository;


import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.bardinpetr.itmo.lab3.app.auth.UserSession;
import ru.bardinpetr.itmo.lab3.app.check.AreaPolygonController;
import ru.bardinpetr.itmo.lab3.data.dao.impl.UserDAO;
import ru.bardinpetr.itmo.lab3.data.models.PointResult;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Stream;

@Named("pointRepo")
@RequestScoped
@Data
@Slf4j
public class PointRepository implements Serializable {

    @Inject
    private UserDAO userDAO;

    @Inject
    private AreaPolygonController areaPolygonController;

    @Inject
    private UserSession session;

    /**
     * Retrieve points for user and area config
     * User and AreaConfig supplied from requestScope and AreaPolygonController accordingly
     */
    public List<PointResult> getCurrentPoints() {
        if (!session.isLoggedIn()) return List.of();
        var pts = userDAO.getPointResults(session.getUser());
        return pts
                .stream()
                .filter(i -> i.getArea().equals(areaPolygonController.getAreaConfig()))
                .toList();
    }

    /**
     * Retrieve points for user
     */
    public List<PointResult> getAllPoints() {
        if (!session.isLoggedIn()) return List.of();
        return userDAO.getPointResults(session.getUser());
    }

    /**
     * Retrieve all points in DB
     */
    public Stream<PointResult> getAllUsersPoints() {
        return userDAO
                .findAll().stream()
                .flatMap(u -> userDAO.getPointResults(u).stream());
    }

    /**
     * Add PointResult to storage of user specified in requestScope
     */
    public void storePointResult(PointResult result) {
        if (!session.isLoggedIn()) return;

        var user = session.getUser();
        user.getPointResults().add(result);
        userDAO.update(user);
    }

    /**
     * Remove all PointResult from storage of user specified in requestScope
     */
    public void removePoints() {
        if (!session.isLoggedIn()) return;
        var user = session.getUser();
        user.getPointResults().clear();
        userDAO.update(user);
    }
}
