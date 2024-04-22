# Use a base image with Java and Maven pre-installed
FROM adoptopenjdk:17-jdk-hotspot AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven configuration files
COPY pom.xml .

# Copy the source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Use a lightweight base image with Java pre-installed
FROM adoptopenjdk:17-jre-hotspot

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the previous stage
COPY --from=build /app/target/linkk.jar .

# Expose the port that the Spring Boot application runs on
EXPOSE 8080

# Define the command to run the application
CMD ["java", "-jar", "linkk.jar"]
