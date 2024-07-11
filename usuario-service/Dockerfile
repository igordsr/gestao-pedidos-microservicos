# Stage 1: Maven build
FROM maven:3.8.4 AS builder

WORKDIR /app

COPY pom.xml .

COPY src ./src

RUN mvn clean package

# Stage 2: OpenJDK image for running the application
FROM openjdk:17

WORKDIR /app

# Copy the JAR file from the previous stage
COPY --from=builder /app/target/*.jar /app/usuario-service-api.jar

# Command to execute the project when the container starts
CMD ["java", "-jar", "/app/usuario-service-api.jar"]
