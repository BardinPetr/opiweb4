package ru.bardinpetr.itmo.lab3.mbean;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.bardinpetr.itmo.lab3.data.models.PointResult;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import java.io.Serializable;

@Getter
@ApplicationScoped
@Slf4j
public class PointCountMonitor
        extends NotificationBroadcasterSupport
        implements PointCountMonitorMBean, Serializable {
    public static final String NOTIFICATION_TYPE = "ru.bardinpetr.itmo.lab3.COUNT_MOD";

    @Setter
    private volatile int eventTriggerCount = 15;
    private volatile int invalidCount = 0;
    private volatile int outsideCount = 0;
    private volatile int insideCount = 0;
    private volatile int sequenceNumber = 0;

    @PostConstruct
    private void init() {
        log.warn("Initialized Point Count MBean");
        MBeanHelper.register(this);
    }

    @Override
    public int getClicksCount() {
        return getInsideCount() + getOutsideCount();
    }

    @Override
    public synchronized void reset() {
        invalidCount = 0;
        outsideCount = 0;
        insideCount = 0;
    }

    public synchronized void newInvalid() {
        invalidCount++;
    }

    public synchronized void newValidPoint(PointResult result) {
        log.info("Registered click in MBean");

        if (result.getIsInside())
            insideCount++;
        else
            outsideCount++;

        var count = getClicksCount();
        if (count % eventTriggerCount == 0) {
            sendNotification(new Notification(
                    NOTIFICATION_TYPE,
                    this, sequenceNumber++, System.currentTimeMillis(),
                    "Reached count dividing by %d: %d".formatted(eventTriggerCount, count)
            ));
        }
    }


}
