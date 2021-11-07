FROM openjdk:11 as spring-nosql
COPY ./target/spring-boot-rest-2-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar", "app.jar"]