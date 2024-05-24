package ru.bardinpetr.itmo.lab3.mbean;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@ApplicationScoped
@Slf4j
public class PointTimeMonitor implements PointTimeMonitorMBean {

    @Getter
    private Double meanBetweenClickTime = 0.0;
    @Getter
    private int count = 0;

    private LocalDateTime lastClick = null;


    @PostConstruct
    private void init() {
        log.warn("Initialized Point Time MBean");
        MBeanHelper.register(this);
    }


    @Override
    public synchronized void reset() {
        lastClick = null;
        meanBetweenClickTime = 0.0;
        count = 0;
    }

    @Override
    public long getJVMUptimeMillis() {
        return ManagementFactory.getRuntimeMXBean().getUptime();
    }

    public synchronized void click(LocalDateTime time) {
        log.info("Monitor registered click at {}", time);

        if (lastClick != null) {
            var newDelta = lastClick.until(time, ChronoUnit.MILLIS);
            meanBetweenClickTime = (meanBetweenClickTime * count + newDelta) / (count + 1);
            count++;
        }

        lastClick = time;
    }
}
