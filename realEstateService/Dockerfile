# Use the official OpenJDK base image
FROM openjdk:21-ea-14-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file into the container
COPY target/realEstateService-0.0.1-SNAPSHOT.jar ./app.jar

# Expose the application port
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]
