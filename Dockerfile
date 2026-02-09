FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
RUN ./gradlew --no-daemon dependencies
COPY src src
ENTRYPOINT ["./gradlew"]