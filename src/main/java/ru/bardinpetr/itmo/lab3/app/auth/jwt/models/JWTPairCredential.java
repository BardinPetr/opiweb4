package ru.bardinpetr.itmo.lab3.app.auth.jwt.models;

import jakarta.security.enterprise.credential.Credential;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JWTPairCredential implements Credential, Serializable {

    private Optional<JWTWrapper> accessToken;
    private Optional<JWTWrapper> refreshToken;

    @Override
    public boolean isValid() {
        return accessToken.isPresent() || refreshToken.isPresent();
    }
}
