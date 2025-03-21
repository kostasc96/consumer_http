# Stage 1: Build stage
FROM openjdk:11-slim AS build

# Install sbt from official repo
RUN apt-get update && \
    apt-get install -y curl gnupg && \
    echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | tee /etc/apt/sources.list.d/sbt.list && \
    curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x99E82A75642AC823" | apt-key add - && \
    apt-get update && \
    apt-get install -y sbt

WORKDIR /app

# Copy build files and download deps first
COPY build.sbt .
COPY project ./project
RUN sbt update

# Now add sources and build the fat jar
COPY src ./src
RUN sbt assembly

# Stage 2: Runtime stage
FROM openjdk:11-jre-slim

WORKDIR /app
COPY --from=build /app/target/scala-2.12/consumer_http-assembly-0.1.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]