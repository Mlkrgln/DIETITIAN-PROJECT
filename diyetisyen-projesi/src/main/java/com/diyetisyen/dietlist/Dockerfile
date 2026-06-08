# 1. Build aşaması
FROM maven:3.9.2-eclipse-temurin-17 AS build

WORKDIR /app

# Kaynakları kopyala
COPY pom.xml .
COPY src ./src

# Package oluştur, testleri atla
RUN mvn clean package -DskipTests

# 2. Runtime aşaması
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Build aşamasından jar'ı al
COPY --from=build /app/target/*.jar app.jar

# Portu her modüle göre expose et
EXPOSE 8083  # auth için, diğer modüllerde 8081, 8082, 8083, 8084

ENTRYPOINT ["java", "-jar", "app.jar"]