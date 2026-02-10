# Build stage
FROM docker.io/maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM docker.io/eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/aws-lambda-spring-boot-1.0.0.jar app.jar

# Add wait-for-it script for dependencies
RUN apk add --no-cache bash curl

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
