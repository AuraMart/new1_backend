# Start with the official OpenJDK 17 image as the base image
FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the Maven-built JAR file into the container
COPY target/dream-shops-0.0.1.jar /app/dream-shops-0.0.1.jar

# Expose the port your Spring Boot application runs on
EXPOSE 9191

# Run the application
ENTRYPOINT ["java", "-jar", "/app/dream-shops-0.0.1.jar"]
