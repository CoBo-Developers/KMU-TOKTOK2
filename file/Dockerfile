FROM openjdk:17-jdk

CMD ["./gradlew", "clean", "build", "-x", "test"]

VOLUME /file

ARG JAR_FILE=build/libs/file-0.0.1-SNAPSHOT.war

COPY ${JAR_FILE} file.jar

EXPOSE 12043

ENTRYPOINT ["java", "-jar", "/file.jar"]