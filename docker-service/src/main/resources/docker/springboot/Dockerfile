# 빌드
FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /app

# 애플리케이션 소스 코드를 복사
COPY . .

# Gradle Wrapper를 사용해 JAR 파일 빌드
RUN ./gradlew clean build -x test --no-daemon

# 실행
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# 빌드 결과물(JAR 파일)을 실행 단계로 복사
COPY --from=builder /app/build/libs/*.jar /app/app.jar

# 포트 노출
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]