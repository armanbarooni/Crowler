# Stage 1: Build
FROM focker.ir/maven:3.8.6-openjdk-11 AS build
WORKDIR /app

# Copy the Maven project files and build the application
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM focker.ir/openjdk:9-jdk
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port your application runs on
EXPOSE 8080

# Set the default command to run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
