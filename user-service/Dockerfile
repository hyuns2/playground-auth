# 빌드
FROM eclipse-temurin:25-jdk AS builder

WORKDIR /build

# 의존성 캐싱 최적화를 위해 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts ./

RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon

COPY . .
RUN chmod +x gradlew
RUN ./gradlew build -x test --no-daemon

# 실행
FROM eclipse-temurin:25-jre

# curl 설치
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY --from=builder /build/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
