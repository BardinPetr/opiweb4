package ru.bardinpetr.itmo.lab3.app.canvas;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;
import ru.bardinpetr.itmo.lab3.app.canvas.dto.CanvasPointResult;
import ru.bardinpetr.itmo.lab3.app.check.PointCheckController;
import ru.bardinpetr.itmo.lab3.app.check.models.PointConstraints;
import ru.bardinpetr.itmo.lab3.data.dto.AreaConfigDTO;
import ru.bardinpetr.itmo.lab3.data.dto.PointCheckRequestDTO;
import ru.bardinpetr.itmo.lab3.data.repository.PointRepository;

import java.io.Serializable;
import java.time.ZoneId;
import java.util.List;

import static ru.bardinpetr.itmo.lab3.utils.ParseDouble.safeParseDouble;


@Named("canvasCheckController")
@RequestScoped
@Slf4j
public class CanvasCheckController extends JSRemoteController implements Serializable {
    @Inject
    private AreaConfigDTO config;
    @Inject
    private PointRepository repository;
    @Inject
    private PointCheckController controller;
    @Inject
    private PointConstraints pointConstraints;

    public void pointClicked() {
        var x = safeParseDouble(getParam("x"));
        var y = safeParseDouble(getParam("y"));
        var r = safeParseDouble(getParam("r"));

        var area = new AreaConfigDTO();
        area.setR(r);
        var point = new PointCheckRequestDTO();
        point.setX(x);
        point.setY(y);

        log.info("JS request 'pointClicked': from canvas: {} {}", area, point);
        var result = controller.doCheck(point, area);
        if (result == null) {
            sendParam("result", null);
            return;
        }

        var send = new JSONObject(CanvasPointResult.of(result));
        log.info("JS request 'pointClicked': sending {}", send);
        sendParam("result", send);
    }

    public void getR() {
        log.info("JS request 'getR': sending R={}", config.getR());
        sendParam("r", config.getR());
    }

    public void getPoints() {
        var fromTime = getParam("from");
        if (fromTime == null)
            fromTime = "0";
        var timestamp = Long.parseLong(fromTime) / 1000 - 1;

        var src = repository
                .getAllPoints()
                .stream()
                .filter(i ->
                        i.getTimestamp()
                                .atZone(ZoneId.systemDefault())
                                .toEpochSecond() > timestamp
                )
                .map(CanvasPointResult::of)
                .toList();

        var pts = new JSONArray(src);
        log.info("JS request 'getCurrentPoints': sending {}", pts);
        sendParam("points", pts);
    }

    public void getConstraints() {
        var pts = new JSONArray(List.of(pointConstraints.getRRange(), pointConstraints.getXRange(), pointConstraints.getYRange()));
        log.info("JS request 'getConstraints': sending {}", pts);
        sendParam("constraints", pts);
    }
}
