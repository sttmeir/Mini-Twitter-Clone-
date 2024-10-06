FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/nottify-0.0.1-SNAPSHOT.jar /app/nottify.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/nottify.jar"]
