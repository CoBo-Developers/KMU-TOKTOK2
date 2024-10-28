FROM openjdk:17-jdk

CMD ["./gradlew", "clean", "build", "-x", "test"]

VOLUME /writing

ARG JAR_FILE=build/libs/writing-0.0.1-SNAPSHOT.war

COPY ${JAR_FILE} writing.jar

EXPOSE 12042

ENTRYPOINT ["java", "-jar", "/writing.jar"]