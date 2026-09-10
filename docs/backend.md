# バックエンド環境

## Springプロファイルの設定

リポジトリ直下の`.env.example`をコピーして`.env`を作成し、使用するプロファイルを設定してください。

### Cognito認証を利用する場合

```dotenv
SPRING_PROFILES_ACTIVE=dev,cognito
```

`hanrolink-backend/.env.example`をコピーして`.env`を作成し、Cognitoの設定値を入力してください。

```dotenv
COGNITO_ISSUER_URI=https://cognito-idp.{リージョン}.amazonaws.com/{ユーザープールID}
COGNITO_CLIENT_ID={クライアントID}
AWS_REGION={リージョン}
```

S3・CloudFrontも利用する場合は、すべてのプロファイルを有効にします。

```dotenv
SPRING_PROFILES_ACTIVE=dev,cognito,s3,cloudfront
```
