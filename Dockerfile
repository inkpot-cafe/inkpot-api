FROM gradle:jre15 AS build
COPY . .
RUN ./gradlew build

FROM openjdk:15-alpine
COPY --from=build /home/gradle/build/inkpot-api-1.0-SNAPSHOT-runner.jar inkpot-api-1.0-SNAPSHOT-runner.jar
COPY --from=build /home/gradle/build/lib lib
CMD java -jar inkpot-api-1.0-SNAPSHOT-runner.jar