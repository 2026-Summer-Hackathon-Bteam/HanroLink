# ---- ビルドステージ (通常ビルド時に使用) ----
FROM eclipse-temurin:25-jdk AS build

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN chmod +x mvnw
# go-offline とパッケージをまとめて実行
RUN ./mvnw dependency:go-offline && ./mvnw clean package -DskipTests

COPY src src

# ---- 実行ステージ: build からコピーするパターン ----
FROM eclipse-temurin:25-jre AS runtime-from-build
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]

# ---- 実行ステージ: 事前に作成した JAR をコンテキストから直接コピーするパターン ----
# 例: すでにローカルで `hanrolink-backend/target/*.jar` がある環境で
# docker buildx build --target runtime-from-context -f hanrolink-backend/Dockerfile \
#   -t <repo>:prod --push hanrolink-backend
FROM eclipse-temurin:25-jre AS runtime-from-context
WORKDIR /app
COPY app.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]