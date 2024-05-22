FROM quay.io/wildfly/wildfly:29.0.1.Final-jdk17

WORKDIR /app

RUN /opt/jboss/wildfly/bin/add-user.sh user pass --silent

RUN curl -o /app/postgresql.jar https://jdbc.postgresql.org/download/postgresql-42.6.0.jar
COPY configure.cli /app/configure.cli
RUN /opt/jboss/wildfly/bin/jboss-cli.sh --connect --file=/app/configure.cli

CMD /opt/jboss/wildfly/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0
