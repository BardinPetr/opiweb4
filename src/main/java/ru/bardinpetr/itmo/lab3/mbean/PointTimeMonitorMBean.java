package ru.bardinpetr.itmo.lab3.mbean;

public interface PointTimeMonitorMBean {
    Double getMeanBetweenClickTime();

    void reset();

    long getJVMUptimeMillis();
}
