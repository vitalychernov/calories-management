# --- Build stage ---
FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /app

# Cache dependencies separately from source
COPY pom.xml .
RUN mvn dependency:go-offline -P heroku -q

COPY src ./src
RUN mvn package -P heroku -DskipTests -q

# --- Run stage ---
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

COPY --from=build /app/target/topjava.war .
COPY --from=build /app/target/dependency/webapp-runner.jar .
COPY config ./config

ENV PORT=8080
ENV TOPJAVA_ROOT=/app
EXPOSE 8080

CMD java $JAVA_OPTS \
    -Dspring.profiles.active="datajpa,heroku" \
    -jar webapp-runner.jar \
    --port $PORT \
    topjava.war
