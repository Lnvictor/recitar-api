FROM maven:latest
LABEL authors="Victor"

COPY . .

EXPOSE 8080

RUN ["mvn", "package", "-DskipTests"]

CMD ["java", "-jar", "./target/recitar-api-0.0.1-SNAPSHOT.jar"]