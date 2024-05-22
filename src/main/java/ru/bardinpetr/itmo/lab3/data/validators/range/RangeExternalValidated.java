package ru.bardinpetr.itmo.lab3.data.validators.range;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.primefaces.validate.bean.ClientConstraint;
import ru.bardinpetr.itmo.lab3.app.check.models.PointConstraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {RangeExternalValidator.class})
@ClientConstraint(resolvedBy=RangeExternalValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface RangeExternalValidated {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    PointConstraints.ConstraintType value();
}
