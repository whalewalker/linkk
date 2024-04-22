# Use a base image with Java and Maven pre-installed
FROM maven:3.8.4-openjdk-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven configuration files
COPY pom.xml .

# Copy the source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Use a lightweight base image with Java pre-installed
FROM openjdk:17-jre-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the previous stage
COPY --from=build /app/target/linkk.jar .

# Expose the port that the Spring Boot application runs on
EXPOSE 8080

# Define the command to run the application
CMD ["java", "-jar", "linkk.jar"]
