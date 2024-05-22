package ru.bardinpetr.itmo.lab3.data.dto;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import lombok.Data;
import ru.bardinpetr.itmo.lab3.data.validators.range.RangeExternalValidated;

import java.io.Serializable;
import java.util.Set;

import static ru.bardinpetr.itmo.lab3.app.check.models.PointConstraints.ConstraintType;

@Data
@Named("areaConfig")
@SessionScoped
public class AreaConfigDTO implements Serializable {
    @RangeExternalValidated(ConstraintType.R)
    private Double r;

    @PostConstruct
    void init() {
        r = 2.0;
    }

    public Set<ConstraintViolation<AreaConfigDTO>> validate() {
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            return factory.getValidator().validate(this);
        }
    }
}
