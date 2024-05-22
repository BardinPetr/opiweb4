package ru.bardinpetr.itmo.lab3.data.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@Entity
@NoArgsConstructor
public class Role implements Serializable {
    @Id
    private String value;

    public static Role of(String name) {
        var r = new Role();
        r.setValue(name);
        return r;
    }
}
