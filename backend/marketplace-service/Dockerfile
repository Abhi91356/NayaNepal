# Stage 1: Build
FROM openjdk:21-jdk-slim as builder
WORKDIR /app
COPY . .
# Assuming Gradle wrapper is present in the service directory or root
RUN ./gradlew assemble

# Stage 2: Run
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8081
CMD ["java", "-jar", "app.jar"]
