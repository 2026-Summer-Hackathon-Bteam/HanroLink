# バックエンド環境

## Cognito認証の切り替え

Cognito認証はデフォルトで有効です。

`hanrolink-backend/.env`に以下の設定を追加してください。

```dotenv
COGNITO_ISSUER_URI=https://cognito-idp.{リージョン}.amazonaws.com/{ユーザープールID}
COGNITO_CLIENT_ID={クライアントID}
COGNITO_REGION={リージョン}
```

### Cognito認証を無効にする場合

```yaml
backend:
  environment:
    SPRING_PROFILES_ACTIVE: "dev,local-no-auth"
```
