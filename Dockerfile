## Stage 1 : build with maven builder image with native capabilities
FROM quay.io/quarkus/centos-quarkus-maven:21.0.0-java11 AS build
COPY . /usr/src/app/
USER root
RUN chown -R quarkus /usr/src/app
USER quarkus
RUN gradle -b /usr/src/app/build.gradle clean buildNative

## Stage 2 : create the docker final image
FROM registry.access.redhat.com/ubi8/ubi-minimal
WORKDIR /work/
COPY --from=build /usr/src/app/build/*-runner /work/application

# set up permissions for user `1001`
RUN mkdir /work/data

EXPOSE 8080

CMD ["./application", "-Dquarkus.http.host=0.0.0.0"]
