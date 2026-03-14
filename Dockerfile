FROM alpine/java:21-jdk

RUN apk --no-cache add curl maven

EXPOSE 5000

WORKDIR /app

COPY pom.xml .

COPY src src

RUN mvn package -DskipTests && cp target/*jar target/application.jar

ENTRYPOINT ["java", "-jar", "target/application.jar"]