
# Step 1: Build JAR inside Docker
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Step 2: Run JAR in Lightweight JDK
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/codezen-backend-1.0.0.jar codezen-backend.jar

EXPOSE 8080
CMD ["java", "-jar", "codezen-backend.jar"]
