package ru.bardinpetr.itmo.lab3.utils;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import ru.bardinpetr.itmo.lab3.utils.ArrayUtils;

import java.io.Serializable;

@Named("checkUtils")
@RequestScoped
public class UICheckUtils implements Serializable {
    @Inject
    private FacesContext context;

    public boolean isFormValid(String formId) {
        if (!context.isValidationFailed())
            return true;
        return ArrayUtils
                .streamFromIterator(context.getClientIdsWithMessages())
                .noneMatch(i -> i.contains(formId));
    }
}
