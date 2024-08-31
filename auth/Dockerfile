FROM openjdk:17-jdk

CMD ["./gradlew", "clean", "build", "-x", "test"]

VOLUME /auth

ARG JAR_FILE=build/libs/auth-0.0.1-SNAPSHOT.war

COPY ${JAR_FILE} auth.jar

EXPOSE 12040

ENTRYPOINT ["java", "-jar", "/auth.jar"]