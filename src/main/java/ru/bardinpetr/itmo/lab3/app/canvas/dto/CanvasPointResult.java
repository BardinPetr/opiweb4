package ru.bardinpetr.itmo.lab3.app.canvas.dto;

import lombok.Data;
import ru.bardinpetr.itmo.lab3.data.models.PointResult;

import java.io.Serializable;

@Data
public class CanvasPointResult implements Serializable {

    private Double x;
    private Double y;
    private Double r;
    private Boolean inside;

    public static CanvasPointResult of(PointResult src) {
        var res = new CanvasPointResult();
        res.setX(src.getPoint().getX());
        res.setY(src.getPoint().getY());
        res.setR(src.getArea().getR());
        res.setInside(src.getIsInside());
        return res;
    }
}
