# HanroLink

**食品関連の小規模サプライヤーとバイヤーが、商品検索や募集を通じて商談を始められるBtoBマッチングプラットフォーム**

## スクリーンショット
- 商品検索
<img width="350" alt="商品検索画面" src="https://github.com/user-attachments/assets/7f786889-3add-4e52-b9e1-845c98150e92" />

- 商品詳細
<img width="350" alt="商品詳細画面" src="https://github.com/user-attachments/assets/c93ba7e5-a416-4392-82e7-80d2ac9a7058" />

- ダッシュボード
<img width="350" alt="ダッシュボード画面" src="https://github.com/user-attachments/assets/6a40559e-1ce0-431d-a1d3-89f93962c8ce" />


## デモ動画

**▶[デモ動画を見る（MP4、5分21秒）](https://github.com/2026-Summer-Hackathon-Bteam/HanroLink/releases/download/demo-video-1/HanroLink_demo.mp4)（クリックすると動画がダウンロードされます）**

> プライバシー保護のため、動画内の一部に黒塗り処理を施しています。動画内に表示される `.test` ドメインのメールアドレスは、テスト用途に予約されたドメインを使用した架空のデモデータです。撮影に使用した環境内の登録データおよび認証情報は、撮影後に適切に削除または初期化しています。

## 概要

HanroLinkは、食品関連の小規模サプライヤーの販路拡大と、バイヤーの商品探索を支援するBtoBマッチングプラットフォームです。

サプライヤーは商品情報を、バイヤーは募集情報を登録できます。バイヤーは商品に、サプライヤーは募集情報に対して商談希望を送信でき、受信側が承諾するとチャットを開始できます。

月ごとの供給可能数量と調達希望数量を表示することで、取引条件に合う相手を探しやすくしています。

## 解決したい課題

- 小規模サプライヤーは営業力や認知度が限られ、販路を広げにくい
- 提供できる時期や数量が限られるため、条件の合うバイヤーに商品が届きにくい
- バイヤーは、企画の時期や必要数量に合う商品を効率よく探しにくい

## 課題を解決する主な機能

- **商品・募集情報による相互探索**  
  サプライヤーは商品情報を登録して募集情報を検索し、バイヤーは募集情報を登録して商品情報を検索できます。

- **時期・数量の可視化**  
  月ごとの供給可能数量と調達希望数量を表示し、取引条件に合う相手を探しやすくします。

- **双方向の商談希望**  
  バイヤーは商品に、サプライヤーは募集情報に対して商談希望を送信できます。

- **承諾後のチャット**  
  商談希望を受け取った相手が承諾すると、チャットで具体的な商談を開始できます。

- **事業者の確認・承認**  
  管理者が新規登録事業者を確認・承認し、商談の土台となる信頼性を確保します。

## 開発環境について

### Backend

[バックエンド環境](docs/backend.md)

- Java 25
- Spring Boot 4.1.x
- Maven Wrapper（mvnw）

### Database

- PostgreSQL 17
- Docker Compose

### Frontend

- React 19.x
- Vite 8.1.x
- Tailwind CSS 5.x

### 前提条件

以下がインストールされていることを確認してください。

- Git
- Docker Desktop
- Visual Studio Code（推奨）

Docker Desktopは起動した状態で作業してください。

### 初回準備

1. リポジトリをクローンします。
2. `.env.example` をコピーして `.env` を作成します。
   ```bash
   cp .env.example .env
   ```
3. `.env`を開き、必要な環境変数を設定します。API全体を動作確認する場合は、ルートの`.env`に`SPRING_PROFILES_ACTIVE=dev,cognito,s3,cloudfront`を設定し、`hanrolink-backend/.env`に開発用AWS環境の設定値を入力してください。

開発環境の起動方法は、次の「開発環境の起動」を参照してください。

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
