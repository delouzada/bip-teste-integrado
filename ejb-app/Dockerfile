FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY ejb-module ./ejb-module
COPY ejb-app ./ejb-app
# Inclui backend-module para satisfazer o pom pai, embora nao seja usado na imagem
COPY backend-module ./backend-module
# Constrói apenas o modulo ejb-app (gera o WAR) sem rodar testes
RUN mvn -q -DskipTests -pl ejb-app -am package

FROM quay.io/wildfly/wildfly:latest
COPY --from=build /app/ejb-app/target/ejb-app-1.0.0-SNAPSHOT.war /opt/jboss/wildfly/standalone/deployments/ejb-app.war
EXPOSE 8080
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]
