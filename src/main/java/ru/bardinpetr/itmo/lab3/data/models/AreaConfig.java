package ru.bardinpetr.itmo.lab3.data.models;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class AreaConfig implements Serializable {
    public static Double EPSILON = 1e-6;
    private Double r;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AreaConfig that = (AreaConfig) o;
        return Math.abs(r - that.r) < EPSILON;
    }

    public static AreaConfig of(Double r) {
        var x = new AreaConfig();
        x.setR(r);
        return x;
    }
}
