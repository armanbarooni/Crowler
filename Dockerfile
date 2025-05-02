# Stage 1: Build
FROM focker.ir/maven:3.8.6-openjdk-11 AS build
WORKDIR /app

COPY pom.xml . 
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM focker.ir/openjdk:9-jdk
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
