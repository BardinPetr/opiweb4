package ru.bardinpetr.itmo.lab3.mbean;

public interface PointCountMonitorMBean {
    int getClicksCount();

    int getInsideCount();

    int getOutsideCount();

    int getInvalidCount();

    int getEventTriggerCount();

    void setEventTriggerCount(int trigger);

    void reset();
}
