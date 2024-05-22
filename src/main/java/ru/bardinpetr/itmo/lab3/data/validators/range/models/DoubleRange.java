package ru.bardinpetr.itmo.lab3.data.validators.range.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static ru.bardinpetr.itmo.lab3.data.validators.range.models.RangeType.INCLUSIVE;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoubleRange implements Serializable {
    @NotNull
    private Double min;
    private RangeType minType = INCLUSIVE;
    @NotNull
    private Double max;
    private RangeType maxType = INCLUSIVE;

    @Override
    public String toString() {
        return "%s%.2f, %.2f%s".formatted(
                minType == INCLUSIVE ? "[" : "(",
                min,
                max,
                maxType == INCLUSIVE ? "]" : ")"
        );
    }


}