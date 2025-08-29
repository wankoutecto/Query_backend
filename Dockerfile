# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app
COPY . .

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-alpine

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
