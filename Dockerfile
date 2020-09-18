FROM openjdk
COPY build/libs/inkpot-api-1.0-SNAPSHOT.jar inkpot-api-1.0-SNAPSHOT.jar
CMD java -jar inkpot-api-1.0-SNAPSHOT.jar