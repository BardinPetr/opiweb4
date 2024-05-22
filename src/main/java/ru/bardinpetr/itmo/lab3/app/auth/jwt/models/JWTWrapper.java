package ru.bardinpetr.itmo.lab3.app.auth.jwt.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JWTWrapper {
    private JWTType type;
    private String token;
}
