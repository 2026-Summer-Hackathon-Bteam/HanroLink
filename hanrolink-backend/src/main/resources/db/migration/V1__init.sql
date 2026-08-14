CREATE TYPE business_role AS ENUM (
  'SUPPLIER',
  'BUYER'
);

CREATE TYPE business_review_status AS ENUM (
  'PENDING',
  'APPROVED'
);

CREATE TABLE businesses (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  public_id UUID UNIQUE NOT NULL,
  role business_role NOT NULL,
  review_status business_review_status DEFAULT 'PENDING' NOT NULL,
  name VARCHAR(255) NOT NULL,
  name_kana VARCHAR(255) NOT NULL,
  website_url VARCHAR(255),
  address_postal_code CHAR(7) NOT NULL,
  address_prefecture VARCHAR(255) NOT NULL,
  address_municipality_street VARCHAR(255) NOT NULL,
  address_building VARCHAR(255),
  phone_number VARCHAR(20) NOT NULL,
  created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,

  CONSTRAINT chk_businesses_postal_code
    CHECK (address_postal_code ~ '^[0-9]{7}$')
);

CREATE TABLE business_user_accounts (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  business_id BIGINT NOT NULL,
  public_id UUID UNIQUE NOT NULL,
  identity_provider_subject VARCHAR(255) UNIQUE NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  first_name VARCHAR(255) NOT NULL,
  last_name_kana VARCHAR(255) NOT NULL,
  first_name_kana VARCHAR(255) NOT NULL,
  phone_number VARCHAR(20) NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,

  CONSTRAINT uq_business_user_accounts_business
    UNIQUE (business_id),

  CONSTRAINT fk_business_user_accounts_business
    FOREIGN KEY (business_id)
    REFERENCES businesses(id)
);

CREATE TABLE regions (
  id SMALLINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  name VARCHAR(50) UNIQUE NOT NULL,
  sort_order SMALLINT UNIQUE NOT NULL,
  created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE product_category_groups (
  id SMALLINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  name VARCHAR(50) UNIQUE NOT NULL,
  sort_order SMALLINT NOT NULL,
  created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE product_categories (
  id SMALLINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  product_category_group_id SMALLINT NOT NULL,
  name VARCHAR(50) UNIQUE NOT NULL,
  sort_order SMALLINT NOT NULL,
  created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,

  CONSTRAINT fk_product_categories_product_category_group
    FOREIGN KEY (product_category_group_id)
    REFERENCES product_category_groups(id)
);

CREATE TYPE product_expiration_type AS ENUM (
  'BEST_BEFORE',
  'USE_BY',
  'NOT_APPLICABLE'
);

CREATE TYPE storage_type AS ENUM (
  'AMBIENT',
  'REFRIGERATED',
  'FROZEN'
);

CREATE TABLE products (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  supplier_business_id BIGINT NOT NULL,
  product_category_id SMALLINT NOT NULL,
  main_ingredient_region_id SMALLINT NOT NULL,
  name VARCHAR(255) NOT NULL,
  main_image_storage_key VARCHAR(255) UNIQUE NOT NULL,
  content_quantity VARCHAR(255) NOT NULL,
  expiration_type product_expiration_type NOT NULL,
  shelf_life_days SMALLINT,
  storage_type storage_type NOT NULL,
  desired_retail_price INT NOT NULL,
  allergy_information VARCHAR(255),
  certification_information TEXT,
  case_size VARCHAR(255),
  units_per_case INT,
  minimum_order_quantity INT,
  shipping_lead_time_days SMALLINT,
  sales_area_restriction VARCHAR(255),
  hidden_at TIMESTAMPTZ,
  created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,

  CONSTRAINT fk_products_supplier_business
    FOREIGN KEY (supplier_business_id)
    REFERENCES businesses(id),
  CONSTRAINT fk_products_product_category
    FOREIGN KEY (product_category_id)
    REFERENCES product_categories(id),
  CONSTRAINT fk_products_main_ingredient_region
    FOREIGN KEY (main_ingredient_region_id)
    REFERENCES regions(id)
);

CREATE TABLE monthly_supply_capacities (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  product_id BIGINT NOT NULL,
  target_month DATE NOT NULL,
  available_quantity INT NOT NULL,
  created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,

  CONSTRAINT fk_monthly_supply_capacities_product
    FOREIGN KEY (product_id)
    REFERENCES products(id)
    ON DELETE CASCADE
);

CREATE TABLE product_story_section_templates (
  id SMALLINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  image_hint VARCHAR(255) NOT NULL,
  body_help_text VARCHAR(255) NOT NULL,
  body_example VARCHAR(255) NOT NULL,
  sort_order SMALLINT UNIQUE NOT NULL,
  created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE product_stories (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  product_id BIGINT NOT NULL,
  product_story_section_template_id SMALLINT NOT NULL,
  position SMALLINT NOT NULL,
  body VARCHAR(255) NOT NULL,
  image_storage_key VARCHAR(255) UNIQUE NOT NULL,
  created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,

  CONSTRAINT fk_product_stories_product
    FOREIGN KEY (product_id)
    REFERENCES products(id)
    ON DELETE CASCADE,
  CONSTRAINT fk_product_stories_product_story_section_template
    FOREIGN KEY (product_story_section_template_id)
    REFERENCES product_story_section_templates(id)
);

CREATE TABLE procurement_requests (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  buyer_account_id BIGINT NOT NULL,
  product_category_id SMALLINT NOT NULL,
  title VARCHAR(255) NOT NULL,
  description TEXT NOT NULL,
  required_trade_terms TEXT,
  desired_unit_price INT,
  delivery_shelf_life_days SMALLINT,
  created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,

  CONSTRAINT fk_procurement_requests_buyer_account
    FOREIGN KEY (buyer_account_id)
    REFERENCES business_user_accounts(id),
  CONSTRAINT fk_procurement_requests_product_category
    FOREIGN KEY (product_category_id)
    REFERENCES product_categories(id)
);

CREATE TABLE procurement_request_storage_types (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  procurement_request_id BIGINT NOT NULL,
  storage_type storage_type NOT NULL,
  created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,

  CONSTRAINT fk_procurement_request_storage_types_procurement_request
    FOREIGN KEY (procurement_request_id)
    REFERENCES procurement_requests(id)
    ON DELETE CASCADE
);

CREATE TABLE monthly_procurement_quantities (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  procurement_request_id BIGINT NOT NULL,
  target_month DATE NOT NULL,
  desired_quantity INT NOT NULL,
  created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,

  CONSTRAINT fk_monthly_procurement_quantities_procurement_request
    FOREIGN KEY (procurement_request_id)
    REFERENCES procurement_requests(id)
    ON DELETE CASCADE
);

CREATE TYPE file_upload_usage AS ENUM (
  'PRODUCT_MAIN_IMAGE',
  'PRODUCT_STORY_IMAGE',
  'MESSAGE_ATTACHMENT'
);

CREATE TABLE pending_file_uploads (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  public_id UUID UNIQUE NOT NULL,
  business_user_account_id BIGINT NOT NULL,
  storage_key VARCHAR(255) UNIQUE NOT NULL,
  usage file_upload_usage NOT NULL,
  mime_type VARCHAR(100) NOT NULL,
  file_size_bytes BIGINT NOT NULL,
  created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,

  CONSTRAINT fk_pending_file_uploads_business_user_account
    FOREIGN KEY (business_user_account_id)
    REFERENCES business_user_accounts(id)
);

CREATE TABLE pending_file_deletions (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  storage_key VARCHAR(255) UNIQUE NOT NULL,
  created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE procurement_negotiation_requests (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  supplier_account_id BIGINT NOT NULL,
  procurement_request_id BIGINT,
  product_id BIGINT,
  procurement_request_snapshot JSONB NOT NULL,
  product_snapshot JSONB NOT NULL,
  product_main_image_storage_key VARCHAR(255) UNIQUE NOT NULL,
  accepted_at TIMESTAMPTZ,
  created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,

  CONSTRAINT fk_procurement_negotiation_requests_supplier_account
    FOREIGN KEY (supplier_account_id)
    REFERENCES business_user_accounts(id),
  CONSTRAINT fk_procurement_negotiation_requests_procurement_request
    FOREIGN KEY (procurement_request_id)
    REFERENCES procurement_requests(id)
    ON DELETE SET NULL,
  CONSTRAINT fk_procurement_negotiation_requests_product
    FOREIGN KEY (product_id)
    REFERENCES products(id)
    ON DELETE SET NULL
);

CREATE TABLE product_negotiation_requests (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  buyer_account_id BIGINT NOT NULL,
  product_id BIGINT,
  product_snapshot JSONB NOT NULL,
  product_main_image_storage_key VARCHAR(255) UNIQUE NOT NULL,
  accepted_at TIMESTAMPTZ,
  created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,

  CONSTRAINT fk_product_negotiation_requests_buyer_account
    FOREIGN KEY (buyer_account_id)
    REFERENCES business_user_accounts(id),
  CONSTRAINT fk_product_negotiation_requests_product
    FOREIGN KEY (product_id)
    REFERENCES products(id)
    ON DELETE SET NULL
);

CREATE TABLE channels (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  public_id UUID UNIQUE NOT NULL,
  supplier_account_id BIGINT NOT NULL,
  buyer_account_id BIGINT NOT NULL,
  product_negotiation_request_id BIGINT,
  procurement_negotiation_request_id BIGINT,
  name VARCHAR(255) NOT NULL,
  created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,

  CONSTRAINT fk_channels_supplier_account
    FOREIGN KEY (supplier_account_id)
    REFERENCES business_user_accounts(id),
  CONSTRAINT fk_channels_buyer_account
    FOREIGN KEY (buyer_account_id)
    REFERENCES business_user_accounts(id),
  CONSTRAINT fk_channels_product_negotiation_request
    FOREIGN KEY (product_negotiation_request_id)
    REFERENCES product_negotiation_requests(id),
  CONSTRAINT fk_channels_procurement_negotiation_request
    FOREIGN KEY (procurement_negotiation_request_id)
    REFERENCES procurement_negotiation_requests(id)
);

CREATE TABLE messages (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  channel_id BIGINT NOT NULL,
  business_user_account_id BIGINT NOT NULL,
  body TEXT,
  created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,

  CONSTRAINT fk_messages_channel
    FOREIGN KEY (channel_id)
    REFERENCES channels(id),
  CONSTRAINT fk_messages_business_user_account
    FOREIGN KEY (business_user_account_id)
    REFERENCES business_user_accounts(id)
);

CREATE TABLE message_files (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  message_id BIGINT NOT NULL,
  storage_key VARCHAR(255) UNIQUE NOT NULL,
  mime_type VARCHAR(100) NOT NULL,
  display_filename VARCHAR(255),
  file_size_bytes BIGINT,
  created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,
  updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP NOT NULL,

  CONSTRAINT fk_message_files_message_id
    FOREIGN KEY (message_id)
    REFERENCES messages(id)
);
