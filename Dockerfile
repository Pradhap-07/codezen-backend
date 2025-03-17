FROM eclipse-temurin:21-jdk

WORKDIR /app

# Ensure the JAR file name matches your actual build
COPY target/codezen-backend-1.0.0.jar codezen-backend.jar

EXPOSE 8080

CMD ["java", "-jar", "codezen-backend.jar"]

