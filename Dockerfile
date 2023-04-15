FROM azul/zulu-openjdk:8-latest
COPY /build/libs/WebsiteCategorizer-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]