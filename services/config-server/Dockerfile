FROM openjdk:21-jdk-slim
RUN apt-get update && apt-get install -y curl
WORKDIR /app
COPY target/config-server-*.jar config-server.jar
ENTRYPOINT ["java", "-jar", "config-server.jar"]
EXPOSE 8888
HEALTHCHECK --interval=10s --timeout=5s --retries=5 CMD curl -f http://localhost:8888/actuator/health || exit 1
