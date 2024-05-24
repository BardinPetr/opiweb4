package ru.bardinpetr.itmo.lab3.mbean;

import lombok.extern.slf4j.Slf4j;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

@Slf4j
public class MBeanHelper {

    public static boolean register(Object mBean) {
        var server = ManagementFactory.getPlatformMBeanServer();

        var cls = mBean.getClass();
        var name = "%s:name=%s".formatted(cls.getPackageName(), cls.getSimpleName());
        log.warn("Registering MBean {}", name);

        ObjectName objName;
        try {
            objName = new ObjectName(name);
        } catch (MalformedObjectNameException ignored) {
            return false;
        }

        try {
            server.unregisterMBean(objName);
        } catch (InstanceNotFoundException | MBeanRegistrationException ignored) {
        }

        try {
            server.registerMBean(mBean, objName);
        } catch (Exception e) {
            log.error("MBean registration error", e);
            return false;
        }
        return true;
    }
}
