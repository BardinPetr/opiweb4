package ru.bardinpetr.itmo.lab3;

import ru.bardinpetr.itmo.lab3.app.auth.db.utils.PasswordService;
import ru.bardinpetr.itmo.lab3.data.models.AreaConfig;
import ru.bardinpetr.itmo.lab3.data.models.Point;
import ru.bardinpetr.itmo.lab3.data.models.PointResult;
import ru.bardinpetr.itmo.lab3.data.models.User;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public class MockFactory {
    public static User createUser() {
        String username = randomString();
        var user = new User();
        user.setLogin(username);
        user.setPasswordHash(new PasswordService().encode(username));
        return user;
    }


    public static PointResult createPointResult() {
        return createPointResult(createAreaConfig().getR(), Math.random() > 0.5);
    }

    public static PointResult createPointResult(double r, boolean status) {
        var res = new PointResult();
        res.setIsInside(status);
        var ac = new AreaConfig();
        ac.setR(r);
        res.setArea(ac);
        var pt = new Point();
        pt.setX(Math.random());
        pt.setY(Math.random());
        res.setPoint(pt);
        res.setTimestamp(LocalDateTime.now());
        res.setExecutionTime(Duration.ofMillis((long) (Math.random() * 1000)));
        return res;
    }

    public static AreaConfig createAreaConfig() {
        var ac = new AreaConfig();
        ac.setR((double) Math.round(Math.random() * 1000));
        return ac;
    }

    public static String randomString() {
        return UUID.randomUUID().toString();
    }
}
