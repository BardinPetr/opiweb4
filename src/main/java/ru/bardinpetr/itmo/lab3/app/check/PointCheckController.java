package ru.bardinpetr.itmo.lab3.app.check;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import ru.bardinpetr.itmo.lab3.app.auth.UserSession;
import ru.bardinpetr.itmo.lab3.data.dto.AreaConfigDTO;
import ru.bardinpetr.itmo.lab3.data.dto.PointCheckRequestDTO;
import ru.bardinpetr.itmo.lab3.data.models.AreaConfig;
import ru.bardinpetr.itmo.lab3.data.models.Point;
import ru.bardinpetr.itmo.lab3.data.models.PointResult;
import ru.bardinpetr.itmo.lab3.data.models.User;
import ru.bardinpetr.itmo.lab3.data.repository.PointRepository;

import java.io.Serializable;
import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;

@Named("pointCheckController")
@RequestScoped
@Slf4j
public class PointCheckController implements Serializable {

    @Inject
    private AreaPolygonController areaPolygonController;
    @Inject
    private PointRepository pointRepository;
    @Inject
    private PointCheckRequestDTO requestDTO;
    @Inject
    private UserSession session;


    public PointResult doCheck() {
        return doCheck(requestDTO, areaPolygonController.getAreaConfigDTO());
    }

    public PointResult doCheck(PointCheckRequestDTO pointDTO, AreaConfigDTO areaDTO) {
        log.info("User {} requested {} {}", session.getUser().getName(), pointDTO, areaDTO);

        var startTime = LocalDateTime.now();

        var areaValid = pointDTO.validate();
        var requestValid = areaDTO.validate();
        if (!areaValid.isEmpty() || !requestValid.isEmpty()) {
            log.error("Validation failed for R={}/X={}/Y={}", areaDTO.getR(), pointDTO.getX(), pointDTO.getY());
            return null;
        }

        var area = AreaConfig.of(areaDTO.getR());
        var point = Point.of(pointDTO.getX(), pointDTO.getY());

        var status = areaPolygonController
                .getPredicate()
                .test(point);

        log.info("Check finished point={} over R={} with result={}", point, area.getR(), status);

        var res = new PointResult();
        res.setPoint(point);
        res.setArea(area);
        res.setIsInside(status);
        res.setTimestamp(LocalDateTime.now());
        res.setExecutionTime(Duration.between(startTime, res.getTimestamp()));

        pointRepository.storePointResult(res);

        return res;
    }
}
