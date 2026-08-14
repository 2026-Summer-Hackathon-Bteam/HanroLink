import mainVisual from '../../assets/mainvisual.png'
import type {
  ProductSearchResult,
  ProductSearchOptions,
} from './productSearchTypes'
import { productFormOptionsMock } from './productFormOptionMock'

export const productSearchMock = {
  products: [
    {
      id: 1,
      name: '青森県産りんごの手作りジャム',
      businessName: '株式会社ハンロフーズ',
      productCategoryName: 'ジャム・はちみつ',
      mainIngredientRegionName: '東北',
      mainImageUrl: mainVisual,
      monthlySupplyCapacities: [
        { targetMonth: '2026-08', availableQuantity: 100 },
        { targetMonth: '2026-09', availableQuantity: 120 },
        { targetMonth: '2026-10', availableQuantity: 80 },
        { targetMonth: '2026-11', availableQuantity: 60 },
        { targetMonth: '2026-12', availableQuantity: 100 },
        { targetMonth: '2027-01', availableQuantity: 50 },
      ],
    },
    {
      id: 2,
      name: '北海道産ミルクの濃厚プリン',
      businessName: '北のスイーツ株式会社',
      productCategoryName: '洋菓子',
      mainIngredientRegionName: '北海道',
      mainImageUrl: mainVisual,
      monthlySupplyCapacities: [
        { targetMonth: '2026-08', availableQuantity: 200 },
        { targetMonth: '2026-09', availableQuantity: 200 },
        { targetMonth: '2026-10', availableQuantity: 150 },
        { targetMonth: '2026-11', availableQuantity: 150 },
        { targetMonth: '2026-12', availableQuantity: 100 },
        { targetMonth: '2027-01', availableQuantity: 100 },
      ],
    },
    {
      id: 3,
      name: '瀬戸内レモンの焼き菓子セット',
      businessName: '瀬戸内菓子工房',
      productCategoryName: '焼き菓子',
      mainIngredientRegionName: '中国',
      mainImageUrl: mainVisual,
      monthlySupplyCapacities: [
        { targetMonth: '2026-08', availableQuantity: 50 },
        { targetMonth: '2026-09', availableQuantity: 70 },
        { targetMonth: '2026-10', availableQuantity: 100 },
        { targetMonth: '2026-11', availableQuantity: 120 },
        { targetMonth: '2026-12', availableQuantity: 150 },
        { targetMonth: '2027-01', availableQuantity: 80 },
      ],
    },
    {
      id: 4,
      name: '信州産そば粉の生そば',
      businessName: '信州めぐみ食品株式会社',
      productCategoryName: '麺類',
      mainIngredientRegionName: '中部',
      mainImageUrl: mainVisual,
      monthlySupplyCapacities: [
        { targetMonth: '2026-08', availableQuantity: 80 },
        { targetMonth: '2026-09', availableQuantity: 100 },
        { targetMonth: '2026-10', availableQuantity: 120 },
        { targetMonth: '2026-11', availableQuantity: 120 },
        { targetMonth: '2026-12', availableQuantity: 90 },
        { targetMonth: '2027-01', availableQuantity: 70 },
      ],
    },
    {
      id: 5,
      name: '愛媛県産みかんジュース',
      businessName: '四国くだもの農園',
      productCategoryName: 'ソフトドリンク',
      mainIngredientRegionName: '四国',
      mainImageUrl: mainVisual,
      monthlySupplyCapacities: [
        { targetMonth: '2026-08', availableQuantity: 150 },
        { targetMonth: '2026-09', availableQuantity: 150 },
        { targetMonth: '2026-10', availableQuantity: 180 },
        { targetMonth: '2026-11', availableQuantity: 200 },
        { targetMonth: '2026-12', availableQuantity: 200 },
        { targetMonth: '2027-01', availableQuantity: 160 },
      ],
    },
    {
      id: 6,
      name: '九州産さつまいもの和菓子詰め合わせ',
      businessName: '九州おやつ工房',
      productCategoryName: '和菓子',
      mainIngredientRegionName: '九州',
      mainImageUrl: mainVisual,
      monthlySupplyCapacities: [
        { targetMonth: '2026-08', availableQuantity: 40 },
        { targetMonth: '2026-09', availableQuantity: 60 },
        { targetMonth: '2026-10', availableQuantity: 100 },
        { targetMonth: '2026-11', availableQuantity: 140 },
        { targetMonth: '2026-12', availableQuantity: 120 },
        { targetMonth: '2027-01', availableQuantity: 80 },
      ],
    },
  ],

  pagination: {
    page: 1,
    pageSize: 6,
    totalCount: 8,
    totalPages: 3,
  },
} satisfies ProductSearchResult

export const productSearchOptionsMock = {
  productCategoryGroups: productFormOptionsMock.productCategoryGroups,
  productCategories: productFormOptionsMock.productCategories,
  mainIngredientRegions: productFormOptionsMock.mainIngredientRegions,
  storageTypes: productFormOptionsMock.storageTypes,
} satisfies ProductSearchOptions
