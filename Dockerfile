FROM openjdk:17-jdk-slim as base

RUN groupadd -r appuser && useradd -r -g appuser appuser
USER appuser

WORKDIR /app

FROM base as api-gateway
COPY ./out/artifacts/ApiGateway_jar/ApiGateway.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]

FROM base as config-server
COPY ./out/artifacts/ConfigServer_jar/ConfigServer.jar app.jar
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]

FROM base as eureka-server
COPY ./out/artifacts/EurekaServer_jar/EurekaServer.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]

FROM base as notification-service
COPY ./out/artifacts/NotificationService_jar/NotificationService.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]

FROM base as user-service
COPY ./out/artifacts/UserServiceApplication_jar/UserServiceApplication.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]