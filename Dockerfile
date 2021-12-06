# Use the official maven/Java 8 image to create a build artifact: https://hub.docker.com/_/maven
FROM maven:3.6.3 as builder

# Copy local code to the container image.
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build a release artifact.
RUN mvn install -DskipTests
RUN mvn package

# Use the Official OpenJDK image for a lean production stage of our multi-stage build.
# https://hub.docker.com/_/openjdk
# https://docs.docker.com/develop/develop-images/multistage-build/#use-multi-stage-builds
FROM openjdk:17-oraclelinux7

# Copy the jar to the production image from the builder stage.
COPY --from=builder /app/target/2BotOrNot2Bot-Backend*.jar /2BotOrNot2Bot-Backend.jar

# Run the web service on container startup.
CMD ["java", "-Djava.security.egd=file:/dev/./urandom","-jar", "/2BotOrNot2Bot-Backend.jar", "--spring.profiles.active=prod"]