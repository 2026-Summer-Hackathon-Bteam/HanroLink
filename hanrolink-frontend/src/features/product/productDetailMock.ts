import mainVisual from '../../assets/mainvisual.png'
import type { ProductDetail } from './productDetailTypes'

export const productDetailMock = {
  id: 1,
  name: '青森県産りんごの手作りジャム',
  hidden: false,

  productCategory: {
    id: 11,
    name: 'ジャム・はちみつ',
  },

  mainIngredientRegion: {
    id: 2,
    name: '青森県',
  },

  contentQuantity: '150g',

  productExpirationType: {
    value: 'BEST_BEFORE',
    label: '賞味期限',
  },

  shelfLifeDays: 180,

  storageType: {
    value: 'AMBIENT',
    label: '常温',
  },

  desiredRetailPrice: 780,
  allergyInformation: 'りんご',
  certificationInformation: 'HACCP対応工場で製造',
  caseSize: '幅30cm × 奥行25cm × 高さ20cm',
  unitsPerCase: 12,
  minimumOrderQuantity: 1,
  shippingLeadTimeDays: 5,
  salesAreaRestriction: '全国',

  mainImageUrl: mainVisual,

  monthlySupplyCapacities: [
    {
      targetMonth: '2026-08',
      availableQuantity: 120,
    },
    {
      targetMonth: '2026-09',
      availableQuantity: 150,
    },
    {
      targetMonth: '2026-10',
      availableQuantity: 200,
    },
    {
      targetMonth: '2026-11',
      availableQuantity: 180,
    },
    {
      targetMonth: '2026-12',
      availableQuantity: 100,
    },
    {
      targetMonth: '2027-01',
      availableQuantity: 80,
    },
  ],

  productStories: [
    {
      id: 1,
      productStorySectionTemplateId: 1,
      position: 1,
      sectionTitle: '素材・原料',
      body: `青森県産のりんごを使用しています。
寒暖差のある地域で育ったりんごを、香りと酸味のバランスが良い時期に収穫しています。`,
      imageUrl: mainVisual,
    },
    {
      id: 2,
      productStorySectionTemplateId: 2,
      position: 2,
      sectionTitle: '産地・地域性',
      body: `青森県の豊かな自然の中で育ったりんごを使用しています。
地域の魅力と素材のおいしさを伝えられる商品を目指しています。`,
      imageUrl: mainVisual,
    },
    {
      id: 3,
      productStorySectionTemplateId: 3,
      position: 3,
      sectionTitle: '作り手の想い',
      body: `りんご本来の味を季節を問わず楽しんでいただきたいという想いから作りました。
素材の風味を残すため、丁寧に少量ずつ製造しています。`,
      imageUrl: mainVisual,
    },
  ],

  supplier: {
    businessName: '青森りんご食品株式会社',
    businessAddressPrefecture: '青森県',
    businessAddressMunicipalityStreet: '青森市サンプル町1丁目2番3号',
    businessAddressBuilding: 'りんご食品センター',
    businessWebsiteUrl: 'https://example.com',
  },

  permissions: {
    canManage: true,
    canCreateNegotiationRequest: false,
  },

  hasMyActiveNegotiationRequest: false,
} satisfies ProductDetail