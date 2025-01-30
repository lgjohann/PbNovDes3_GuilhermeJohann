FROM openjdk:17-jdk-slim as build

RUN apt-get update && apt-get install -y maven

WORKDIR /app

COPY .. /app

RUN mvn clean install -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/ms-event-manager-0.0.1-SNAPSHOT.jar /app/ms-event-manager.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "ms-event-manager.jar"]