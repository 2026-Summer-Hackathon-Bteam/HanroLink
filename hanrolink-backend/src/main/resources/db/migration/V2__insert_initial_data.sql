INSERT INTO regions (id, name, created_at, updated_at)
OVERRIDING SYSTEM VALUE
VALUES
  (1, '北海道', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (2, '東北', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (3, '関東', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (4, '中部', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (5, '近畿', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (6, '中国', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (7, '四国', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (8, '九州', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

ALTER TABLE regions ALTER COLUMN id RESTART WITH 1000;

INSERT INTO prefectures (id, region_id, name, created_at, updated_at)
OVERRIDING SYSTEM VALUE
VALUES
  (1, 1, '北海道', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (2, 2, '青森県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (3, 2, '岩手県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (4, 2, '宮城県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (5, 2, '秋田県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (6, 2, '山形県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (7, 2, '福島県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (8, 3, '茨城県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (9, 3, '栃木県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (10, 3, '群馬県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (11, 3, '埼玉県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (12, 3, '千葉県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (13, 3, '東京都', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (14, 3, '神奈川県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (15, 4, '新潟県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (16, 4, '富山県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (17, 4, '石川県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (18, 4, '福井県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (19, 4, '山梨県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (20, 4, '長野県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (21, 4, '岐阜県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (22, 4, '静岡県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (23, 4, '愛知県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (24, 5, '三重県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (25, 5, '滋賀県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (26, 5, '京都府', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (27, 5, '大阪府', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (28, 5, '兵庫県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (29, 5, '奈良県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (30, 5, '和歌山県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (31, 6, '鳥取県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (32, 6, '島根県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (33, 6, '岡山県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (34, 6, '広島県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (35, 6, '山口県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (36, 7, '徳島県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (37, 7, '香川県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (38, 7, '愛媛県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (39, 7, '高知県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (40, 8, '福岡県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (41, 8, '佐賀県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (42, 8, '長崎県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (43, 8, '熊本県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (44, 8, '大分県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (45, 8, '宮崎県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (46, 8, '鹿児島県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (47, 8, '沖縄県', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

ALTER TABLE prefectures ALTER COLUMN id RESTART WITH 1000;

INSERT INTO product_category_groups (id, name, sort_order, created_at, updated_at)
OVERRIDING SYSTEM VALUE
VALUES
  (1, '食品', 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (2, 'スイーツ・お菓子', 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (3, 'お酒', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (4, '水・ソフトドリンク', 40, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (5, 'その他', 50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

ALTER TABLE product_category_groups ALTER COLUMN id RESTART WITH 1000;

INSERT INTO product_categories (product_category_group_id, name, sort_order, created_at, updated_at)
VALUES
  (1, '米・雑穀・シリアル', 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1, '麺類', 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1, '野菜', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1, '水産物・水産加工物', 40, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1, '肉・肉加工品', 50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1, '卵・チーズ・乳製品', 60, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1, '果物', 70, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1, '惣菜・レトルト・冷凍', 80, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1, '豆腐・納豆・漬物', 90, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1, 'パン', 100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1, 'ジャム・はちみつ', 110, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1, '粉類', 120, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1, '乾物', 130, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1, 'ダイエットフード', 140, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1, '缶詰・瓶詰', 150, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (1, '調味料', 160, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (2, '洋菓子', 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (2, '和菓子', 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (2, '菓子材料', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (2, 'スナック・駄菓子', 40, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (2, 'おつまみ・珍味', 50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (2, 'ドライフルーツ', 60, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (3, 'ビール・地ビール', 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (3, '焼酎', 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (3, '日本酒', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (3, '梅酒', 40, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (3, 'ワイン', 50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (3, 'ウイスキー', 60, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (3, 'ブランデー', 70, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (3, 'リキュール', 80, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (4, 'ソフトドリンク', 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (4, 'コーヒー・ココア', 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (4, 'ハーブティー', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (4, '日本茶', 40, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (4, '水・ミネラルウォーター', 50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (4, '中国茶', 60, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (4, '紅茶', 70, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (5, 'その他', 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO product_story_section_templates (
  title,
  image_hint,
  body_help_text,
  body_example,
  sort_order,
  created_at,
  updated_at
)
VALUES
  (
    '素材・原料',
    '原材料そのもの、畑、果樹園、牧場、収穫前の素材、生産現場の写真',
    CONCAT(
      'この商品に使っている素材や原料について教えてください。',
      E'\n',
      '産地、品種、収穫時期、鮮度、栽培方法など、素材の良さが伝わる内容があれば入力してください。'
    ),
    '青森県産のりんごを使用しています。寒暖差のある地域で育ったりんごを、香りと酸味のバランスが良い時期に収穫しています。',
    10,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
  ),
  (
    '産地・地域性',
    '地域の風景、農園、港、山、町並み、地域名が伝わる看板や背景',
    CONCAT(
      'この商品が生まれた地域や、地域ならではの特徴について教えてください。',
      E'\n',
      '地域の気候、文化、地元食材、地域フェアで伝えやすい背景などがあれば入力してください。'
    ),
    '地元で長く親しまれている果物を使った商品です。地域フェアや観光向けの売場で、産地の魅力を伝えやすい商品です。',
    20,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
  ),
  (
    '作り手の想い',
    '作り手本人、作業中の様子、工房、家族やスタッフ、商品を手に持っている写真',
    CONCAT(
      'この商品を作っている人の想いや、商品づくりで大切にしていることを教えてください。',
      E'\n',
      'なぜこの商品を作ったのか、どんな人に届けたいのかなどを書いてください。'
    ),
    '地元の果物のおいしさを、季節を問わず楽しんでもらいたいという想いから作りました。素材の味を残すことを大切にしています。',
    30,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
  ),
  (
    '製法・加工の工夫',
    '加工中の様子、工房、機械、手作業、煮込み・乾燥・発酵・包装工程',
    CONCAT(
      'この商品を作る工程や、加工で工夫している点を教えてください。',
      E'\n',
      '味や品質を保つために行っている作業、手間をかけている工程などがあれば入力してください。'
    ),
    '果実感を残すため、加熱時間を調整しながら少量ずつ仕上げています。香りが残るよう、素材の状態を見ながら加工しています。',
    40,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
  ),
  (
    '味・食感の特徴',
    '完成品のアップ、断面、スプーンですくった写真、食感が伝わる写真',
    CONCAT(
      'この商品の味・香り・食感の特徴を教えてください。',
      E'\n',
      '甘さ、酸味、香ばしさ、なめらかさ、食べごたえなど、食べたときの印象が伝わるように入力してください。'
    ),
    'りんごの自然な甘みとほどよい酸味が特徴です。果肉感を残しているため、パンやヨーグルトに合わせても存在感があります。',
    50,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
  ),
  (
    '見た目・パッケージ',
    'パッケージ正面、箱入り商品、ラベル、ギフト包装、売場に並べたイメージ',
    CONCAT(
      '商品の見た目やパッケージの特徴を教えてください。',
      E'\n',
      '売場で目を引く点、ギフトに向く点、陳列しやすい点などがあれば入力してください。'
    ),
    '落ち着いたデザインの瓶入り商品で、地域フェアやギフト売場にも並べやすい見た目です。小さめサイズで手に取りやすい商品です。',
    60,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
  ),
  (
    'おすすめの食べ方・使い方',
    'パンやヨーグルトに添えた写真、料理に使った写真、盛り付け例、使用シーン',
    CONCAT(
      'この商品のおすすめの食べ方や使い方を教えてください。',
      E'\n',
      '家庭用、ホテル朝食、飲食店メニュー、ギフトなど、どんな場面で使いやすいかを入力してください。'
    ),
    'パンやヨーグルトに合わせるほか、チーズや焼き菓子との相性も良い商品です。ホテル朝食や地域フェアの試食提案にも使いやすいです。',
    70,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
  ),
  (
    '売場・企画との相性',
    '売場イメージ、陳列例、POPと並べた写真、ギフト棚、催事コーナー風の写真',
    CONCAT(
      'この商品が合いそうな売場や企画について教えてください。',
      E'\n',
      '地域フェア、季節フェア、ホテル朝食、ギフト、テスト販売など、バイヤーが使い方を想像しやすい内容を入力してください。'
    ),
    '秋の地域フェアや季節限定コーナーに向いています。産地や素材の説明がしやすく、POPや試食販売と組み合わせやすい商品です。',
    80,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
  ),
  (
    '季節性・限定感',
    '季節感のある素材、旬の収穫風景、季節の売場、限定パッケージ',
    CONCAT(
      'この商品の季節性や限定感について教えてください。',
      E'\n',
      'いつの時期に特におすすめか、数量や販売期間に限りがある場合は、その理由も入力してください。'
    ),
    '秋に収穫したりんごを使った季節限定の商品です。初回は300個までの供給となるため、数量限定の地域フェアに向いています。',
    90,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
  );
  