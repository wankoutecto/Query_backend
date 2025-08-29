# Stage 1: Build
FROM maven:3.9.3-eclipse-temurin-18 AS builder

WORKDIR /app
COPY . .

# Make mvnw executable
RUN chmod +x mvnw

# Build the project
RUN ./mvnw clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:18-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
