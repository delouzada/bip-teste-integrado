FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
# Copia poms e modulos necessarios para resolver dependencias locais
COPY pom.xml .
COPY ejb-module ./ejb-module
COPY ejb-app ./ejb-app
COPY backend-module ./backend-module
# Constrói apenas o backend (e dependencias) e ignora testes
RUN mvn -q -DskipTests -pl backend-module -am package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/backend-module/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
