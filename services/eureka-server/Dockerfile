FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/eureka-server-*.jar eureka-server.jar
ENTRYPOINT ["java", "-jar", "eureka-server.jar"]
EXPOSE 8761
