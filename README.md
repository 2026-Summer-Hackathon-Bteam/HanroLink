# HanroLink

## 開発環境について

### Backend

* Java 25
* Spring Boot 4.1.x
* Maven Wrapper（mvnw）

### Database

* PostgreSQL 17
* Docker Compose

### Frontend

* React 19.x
* Vite 8.1.x
* Tailwind CSS 5.x


### ディレクトリ構成

```text
hanrolink
│
├── docker-compose.yml
├── .env
│
├── hanrolink-backend
│   ├── Dockerfile
│   ├── pom.xml
│   └── src
│       └── main
│           ├── java
│           │   └── com
│           │       └── hanrolink
│           └── resources
│               └── application.yml
│
└── hanrolink-frontend
    ├── package.json
    ├── vite.config.ts
    ├── tsconfig.json
    ├── .env
    └── src
```

### 前提条件

以下がインストールされていることを確認してください。

* Git
* Docker Desktop
* Visual Studio Code（推奨）

Docker Desktopは起動した状態で作業してください。

### プロジェクトの取得

```bash
git clone git@github.com:2026-Summer-Hackathon-Bteam/HanroLink.git
```

## 開発環境の起動

### コンテナをビルド・起動

```bash
docker compose up --build
```

初回のみイメージのビルドが実行されます。


```bash
docker compose up -d
```

起動確認

```bash
docker ps
```

以下のコンテナが表示されれば成功です。

```text
hanrolink-postgres
hanrolink-backend
hanrolink-frontend
```

起動後

### Spring Bootの起動確認

```text
http://localhost:8080
```

へアクセスし、Spring Bootが正常に起動していることを確認してください。

### React(Vite)の起動確認

```text
http://localhost:5173
```

へアクセスし、React(Vite)が正常に起動していることを確認してください。

### PostgreSQLへの接続確認

```bash
docker exec -it hanrolink-postgres psql -U hanrolink
```

接続後

```sql
\l
```

でデータベース一覧を確認できます。

終了

```sql
\q
```

### 停止方法

```text
Ctrl + C
```

```bash
docker compose down
```

データを保持したまま停止します。

### データを完全に削除する場合

**注意：データベースの内容がすべて削除されます。**

```bash
docker compose down -v
```
