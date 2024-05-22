package ru.bardinpetr.itmo.lab3.data.validators.range;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.metadata.ConstraintDescriptor;
import org.primefaces.shaded.json.JSONObject;
import org.primefaces.validate.bean.ClientValidationConstraint;
import ru.bardinpetr.itmo.lab3.app.check.models.PointConstraints;
import ru.bardinpetr.itmo.lab3.data.validators.range.models.DoubleRange;

import java.util.Map;

import static ru.bardinpetr.itmo.lab3.data.validators.range.models.RangeType.INCLUSIVE;

public class RangeExternalValidator implements ConstraintValidator<RangeExternalValidated, Double>, ClientValidationConstraint {
    private DoubleRange range;

    @Override
    public void initialize(RangeExternalValidated constraintAnnotation) {
        range = getRange(constraintAnnotation);
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    public DoubleRange getRange(RangeExternalValidated constraintAnnotation) {
        var constraints = CDI.current().select(PointConstraints.class).get();
        return constraints.byType(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (value == null) {
            context
                    .buildConstraintViolationWithTemplate("Value should not be empty")
                    .addConstraintViolation();
            return false;
        }

        var minCheck = value.compareTo(range.getMin());
        var maxCheck = value.compareTo(range.getMax());
        var res = (range.getMinType() == INCLUSIVE ? minCheck >= 0 : minCheck > 0) &&
                (range.getMaxType() == INCLUSIVE ? maxCheck <= 0 : maxCheck < 0);
        if (!res) {
            context
                    .buildConstraintViolationWithTemplate("Value {value}=%.2f not in range %s".formatted(value, range))
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

    @Override
    public Map<String, Object> getMetadata(ConstraintDescriptor<?> constraintDescriptor) {
        var curRange = getRange((RangeExternalValidated) constraintDescriptor.getAnnotation());
        return Map.of(
                "data-range", new JSONObject(curRange).toString()
        );
    }

    @Override
    public String getValidatorId() {
        return RangeExternalValidated.class.getSimpleName();
    }
}
