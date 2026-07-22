# ==========================
# Stage 1 - Build
# ==========================
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

RUN chmod +x mvnw

RUN ./mvnw dependency:go-offline

COPY src src

RUN ./mvnw clean package -DskipTests

# ==========================
# Stage 2 - Runtime
# ==========================
FROM eclipse-temurin:21-jre

RUN groupadd --system spring \
    && useradd --system --gid spring spring

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

RUN mkdir uploads \
    && chown -R spring:spring /app

USER spring

EXPOSE 8081

ENTRYPOINT ["java","-XX:MaxRAMPercentage=75.0","-jar","app.jar"]