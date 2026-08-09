INSERT INTO business_user_accounts (
  business_id,
  public_id,
  identity_provider_subject,
  role,
  review_status,
  last_name,
  first_name,
  last_name_kana,
  first_name_kana,
  phone_number,
  email,
  created_at,
  updated_at
)
VALUES
  (
    (
      SELECT id
      FROM businesses
      WHERE name = 'テスト株式会社'
    ),
    '00000000-0000-0000-0000-000000000001',
    '00000000-0000-0000-0000-000000000001',
    'BUYER',
    'PENDING',
    '鈴木',
    '太郎',
    'スズキ',
    'タロウ',
    '09012345678',
    'test@example.com',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
  );