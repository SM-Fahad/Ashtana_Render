# ----------- Build Stage -----------
FROM maven:3.8.5-openjdk-17 AS build

# Set working directory
WORKDIR /app

# Copy only Maven files first to leverage caching
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Download dependencies (cached if pom.xml doesn't change)
RUN mvn dependency:go-offline -B

# Copy the rest of the project
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# ----------- Run Stage -----------
FROM openjdk:17.0.1-jdk-slim

# Set working directory
WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar app.jar

# Expose port (Render uses $PORT)
ENV PORT=10000
EXPOSE $PORT

# Run the application with the port from environment variable
ENTRYPOINT ["sh", "-c", "java -Dserver.port=$PORT -jar app.jar"]