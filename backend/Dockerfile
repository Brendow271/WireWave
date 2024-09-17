FROM openjdk:17-jdk-alpine
WORKDIR /app
RUN apk add --no-cache maven

COPY pom.xml ./
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn package -DskipTests

ENTRYPOINT ["java", "-jar", "/app/target/wirewave-0.0.1-SNAPSHOT.jar"]
