# バックエンド環境

## Cognito認証の切り替え

Cognito認証はデフォルトで有効です。

`hanrolink-backend/.env.example`をコピーして`.env`を作成し、Cognitoの設定値を入力してください。

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
