FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY mvnw .

RUN chmod +x ./mvnw

COPY pom.xml .

COPY .mvn .mvn

RUN ./mvnw dependency:resolve

COPY src src

RUN ./mvnw package -DskipTests

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=0 /app/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]