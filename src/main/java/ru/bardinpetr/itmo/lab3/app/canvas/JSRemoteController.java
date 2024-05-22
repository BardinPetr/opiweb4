package ru.bardinpetr.itmo.lab3.app.canvas;

import jakarta.faces.annotation.ManagedProperty;
import jakarta.inject.Inject;
import lombok.Setter;
import org.primefaces.PrimeFaces;

import java.util.Map;

public class JSRemoteController {
    @Inject
    @ManagedProperty("#{facesContext.externalContext.requestParameterMap}")
    @Setter
    private Map<String, String> params;

    protected String getParam(String name) {
        return params.getOrDefault(name, null);
    }

    protected void sendParam(String name, Object value) {
        PrimeFaces.current().ajax().addCallbackParam(name, value);
    }
}
