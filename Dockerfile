# FROM openliberty/open-liberty:full-java8-openj9-ubi
# COPY src/main/liberty/config /config/
# ADD target/testwar.war /config/dropins/
# Wildfly
FROM jboss/wildfly:24.0.0.Final
ADD target/testwar.war /opt/jboss/wildfly/standalone/deployments/