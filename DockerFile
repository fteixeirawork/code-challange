# syntax=docker/dockerfile:1

# ---------- build stage ----------
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -q package -DskipTests   # produces target/<artifact>-0.0.1-SNAPSHOT.jar

# ---------- runtime stage ----------
FROM eclipse-temurin:17-jre
COPY --from=build /app/target/*-SNAPSHOT.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
