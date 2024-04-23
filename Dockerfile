# Use the official OpenJDK 17 image as the base image
FROM openjdk:17

# Set the working directory inside the container
WORKDIR /app

# Copy the entire project directory into the container
COPY . /app

# Build the application, skipping the test phase
RUN ./mvnw clean package -DskipTests

# Expose the port on which the application will run
EXPOSE 8080

# Set the entry point for the container
ENTRYPOINT ["java", "-jar", "target/linkk-0.0.1-SNAPSHOT.jar"]