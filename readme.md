## Setup
```shell
jboss-cli.sh --connect --file=configure.cli
```

```shell
#https://stackoverflow.com/questions/51697387/how-to-cpu-profile-wildfly10-using-visualvm
cp /usr/share/visualvm/visualvm/lib/jfluid-server.jar ~/Applications/wildfly/standalone/lib/ext/
nano ~/Applications/wildfly/bin/standalone.conf
JAVA_OPTS="$JAVA_OPTS -Djboss.modules.system.pkgs=$JBOSS_MODULES_SYSTEM_PKGS,org.netbeans.lib.profiler.server,org.jboss.byteman,org.graalvm.visualvm.lib.jfluid.server -Djava.awt.headless=true"
```