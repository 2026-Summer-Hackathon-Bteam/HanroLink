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

* 技術選定中

  * React（候補）
  * Vite（候補）
  * Tailwind CSS（候補）


### ディレクトリ構成

```text
hanrolink/
├── hanrolink-backend/
├── frontend/
├── docker-compose.yml
└── README.md
```

### 前提条件

以下がインストールされていることを確認してください。

* Git
* Docker Desktop
* Java 25
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
```

起動後

### Spring Bootの起動確認

```text
http://localhost:8080
```

へアクセスし、Spring Bootが正常に起動していることを確認してください。


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

Spring Boot

```text
Ctrl + C
```

PostgreSQL

```bash
docker compose down
```

データを保持したまま停止します。

### データを完全に削除する場合

**注意：データベースの内容がすべて削除されます。**

```bash
docker compose down -v
```

### 現在の技術スタック

| 項目         | 技術                |
| ---------- | ----------------- |
| Language   | Java 25           |
| Framework  | Spring Boot 4.1.0 |
| Build Tool | Maven Wrapper     |
| Database   | PostgreSQL 17     |
| Container  | Docker Compose    |
| Frontend   | 技術選定中             |


### 今後追加予定

* React（Vite）
* Tailwind CSS
* JWT認証