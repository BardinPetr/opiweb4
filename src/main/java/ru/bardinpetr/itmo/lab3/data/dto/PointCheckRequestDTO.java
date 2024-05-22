package ru.bardinpetr.itmo.lab3.data.dto;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.bardinpetr.itmo.lab3.data.validators.range.RangeExternalValidated;

import java.io.Serializable;
import java.util.Set;

import static ru.bardinpetr.itmo.lab3.app.check.models.PointConstraints.ConstraintType;

@Named("pointCheckRequest")
@RequestScoped
@Data
public class PointCheckRequestDTO implements Serializable {
    @RangeExternalValidated(ConstraintType.X)
    private Double x;
    @RangeExternalValidated(ConstraintType.Y)
    private Double y;

    public Set<ConstraintViolation<PointCheckRequestDTO>> validate() {
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            return factory.getValidator().validate(this);
        }
    }
}
