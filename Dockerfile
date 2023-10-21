FROM --platform=linux/amd64 eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle
RUN ./gradlew build || return 0
COPY . .

RUN ./gradlew build

FROM --platform=linux/amd64 eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY --from=build /app/build/libs/*.war /app/*.war

ENTRYPOINT ["java", "-jar", "/app/*.war" ]