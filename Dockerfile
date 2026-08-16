# ---------- 1. ASAMA: derleme ----------
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /build

# Once sadece pom.xml kopyala: bagimliliklar degismediyse
# Docker bu katmani onbellekten alir, tekrar indirmez
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Sonra kaynak kodu kopyala ve paketle
COPY src ./src
RUN mvn clean package -DskipTests -B

# ---------- 2. ASAMA: calistirma ----------
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Root olmayan kullanici olustur (guvenlik)
RUN addgroup -S spring && adduser -S spring -G spring

# Derleme asamasindan sadece jar dosyasini al
COPY --from=build /build/target/*.jar app.jar

RUN chown spring:spring app.jar
USER spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
