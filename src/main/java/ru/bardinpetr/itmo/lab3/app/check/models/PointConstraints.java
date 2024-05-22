package ru.bardinpetr.itmo.lab3.app.check.models;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import lombok.Data;
import ru.bardinpetr.itmo.lab3.data.validators.range.models.DoubleRange;

import java.io.Serializable;

import static ru.bardinpetr.itmo.lab3.data.validators.range.models.RangeType.EXCLUSIVE;

@Data
@Named("pointConstraints")
@ApplicationScoped
public class PointConstraints implements Serializable {
    private DoubleRange xRange = new DoubleRange(-5.0, EXCLUSIVE, 5.0, EXCLUSIVE);
    private DoubleRange yRange = new DoubleRange(-3.0, EXCLUSIVE, 5.0, EXCLUSIVE);
    private DoubleRange rRange = new DoubleRange(1.0, EXCLUSIVE, 4.0, EXCLUSIVE);

    public DoubleRange byType(ConstraintType name) {
        return switch (name) {
            case X -> xRange;
            case Y -> yRange;
            case R -> rRange;
        };
    }

    public enum ConstraintType {
        X, Y, R
    }
}
