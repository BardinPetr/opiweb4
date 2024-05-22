package ru.bardinpetr.itmo.lab3.context;

import jakarta.el.ELException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@ApplicationScoped
public class ContextExpressionEvaluator {
    public <T> Optional<T> evaluateExpression(Class<T> targetClass, String expression) {
        var context = FacesContext.getCurrentInstance();
        try {
            var res = context
                    .getApplication()
                    .evaluateExpressionGet(
                            context,
                            "#{" + expression + "}",
                            targetClass
                    );
            return Optional.ofNullable(res);
        } catch (ELException ex) {
            log.error("Bean search by expression '{}' failed: {}", expression, ex.toString());
        }
        return Optional.empty();
    }
}
