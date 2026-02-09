FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine

RUN addgroup -S bffgroup && adduser -S bffuser -G bffgroup
USER bffuser

WORKDIR /app
COPY --from=build /app/build/libs/*.jar bff-app.jar

HEALTHCHECK --interval=30s --timeout=3s \
  CMD wget -q --spider http://localhost:8081/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "bff-app.jar"]