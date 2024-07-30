FROM openjdk:17-jdk

CMD ["./gradlew", "clean", "build", "-x", "test"]

VOLUME /chat

ARG JAR_FILE=build/libs/chat-0.0.1-SNAPSHOT.war

COPY ${JAR_FILE} chat.jar

EXPOSE 12041

ENTRYPOINT ["java", "-jar", "/chat.jar"]