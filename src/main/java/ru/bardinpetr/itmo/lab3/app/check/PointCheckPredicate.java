package ru.bardinpetr.itmo.lab3.app.check;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import ru.bardinpetr.itmo.lab3.data.models.Point;

import java.util.function.Predicate;

/**
 * Represents area check predicate with R=1
 */
@Named("pointCheckPredicate")
@ApplicationScoped
public class PointCheckPredicate implements Predicate<Point> {
    @Override
    public boolean test(Point point) {
        var x = point.getX();
        var y = point.getY();

        if (x >= 0)
            return 0 <= y && y <= 0.5 && x <= 1;

        if (y < 0)
            return Math.hypot(x, y) <= 0.5;

        // y(x) = 0.5 + 0.5 * x
        return y <= (0.5 * (1 + x));
    }
}
