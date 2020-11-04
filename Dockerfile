FROM gradle:jre14 AS build
COPY . .
RUN ./gradlew bootJar

FROM openjdk:16-alpine
COPY --from=build /home/gradle/build/libs/inkpot-api-1.0-SNAPSHOT.jar inkpot-api-1.0-SNAPSHOT.jar
CMD java -jar inkpot-api-1.0-SNAPSHOT.jar