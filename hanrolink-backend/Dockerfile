# ---- ビルドステージ (通常ビルド時に使用) ----
FROM eclipse-temurin:25-jdk AS build

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN chmod +x mvnw
# go-offline とパッケージをまとめて実行
RUN ./mvnw dependency:go-offline
COPY src src
RUN ./mvnw clean package -DskipTests

# ---- 実行ステージ: build からコピーするパターン ----
FROM eclipse-temurin:25-jre AS runtime-from-build
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]

# ---- 実行ステージ: 事前に作成した JAR をコンテキストから直接コピーするパターン ----
FROM eclipse-temurin:25-jre AS runtime-from-context
WORKDIR /app
COPY app.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]