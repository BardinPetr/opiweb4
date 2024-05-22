package ru.bardinpetr.itmo.lab3.context;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;

@ApplicationScoped
@Data
public class ContextProvider {
    @Inject
    private ExternalContext externalContext;
    @Inject
    private SecurityContext securityContext;
    @Inject
    private FacesContext context;

    public HttpServletRequest getRequest() {
        return (HttpServletRequest) externalContext.getRequest();
    }

    public HttpServletResponse getResponse() {
        return (HttpServletResponse) externalContext.getResponse();
    }

    public void sendMessage(FacesMessage.Severity severity, String message) {
        context.addMessage(null, new FacesMessage(severity, message, message));
    }
}
