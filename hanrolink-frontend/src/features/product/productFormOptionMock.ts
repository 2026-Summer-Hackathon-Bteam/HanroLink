import type { SupplierProductFormOptions } from "./productFormTypes"
import { productStoryTemplateMock } from './productStoryTemplateMock'



export const productFormOptionsMock = {
  productCategoryGroups: [
    { id: 1, name: '食品', sortOrder: 10 },
    { id: 2, name: 'スイーツ・お菓子', sortOrder: 20 },
    { id: 3, name: 'お酒', sortOrder: 30 },
    { id: 4, name: '水・ソフトドリンク', sortOrder: 40 },
    { id: 5, name: 'その他', sortOrder: 50 },
  ],

  productCategories: [
    {
      id: 1,
      productCategoryGroupId: 1,
      name: '米・雑穀・シリアル',
      sortOrder: 10,
    },
    {
      id: 2,
      productCategoryGroupId: 1,
      name: '麺類',
      sortOrder: 20,
    },
    {
      id: 3,
      productCategoryGroupId: 1,
      name: '野菜',
      sortOrder: 30,
    },
    {
      id: 7,
      productCategoryGroupId: 1,
      name: '果物',
      sortOrder: 70,
    },
    {
      id: 11,
      productCategoryGroupId: 1,
      name: 'ジャム・はちみつ',
      sortOrder: 110,
    },
    {
      id: 17,
      productCategoryGroupId: 2,
      name: '洋菓子',
      sortOrder: 10,
    },
    {
      id: 18,
      productCategoryGroupId: 2,
      name: '和菓子',
      sortOrder: 20,
    },
    {
      id: 23,
      productCategoryGroupId: 3,
      name: 'ビール・地ビール',
      sortOrder: 10,
    },
    {
      id: 31,
      productCategoryGroupId: 4,
      name: 'ソフトドリンク',
      sortOrder: 10,
    },
    {
      id: 38,
      productCategoryGroupId: 5,
      name: 'その他',
      sortOrder: 10,
    },
  ],

  mainIngredientRegions: [
    { id: 1, name: '北海道', sortOrder: 10 },
    { id: 2, name: '東北', sortOrder: 20 },
    { id: 3, name: '関東', sortOrder: 30 },
    { id: 4, name: '中部', sortOrder: 40 },
    { id: 5, name: '近畿', sortOrder: 50 },
    { id: 6, name: '中国', sortOrder: 60 },
    { id: 7, name: '四国', sortOrder: 70 },
    { id: 8, name: '九州', sortOrder: 80 },
  ],

  productExpirationTypes: [
    { value: 'BEST_BEFORE', label: '賞味期限' },
    { value: 'USE_BY', label: '消費期限' },
    { value: 'NOT_APPLICABLE', label: '該当なし' },
  ],

  storageTypes: [
    { value: 'AMBIENT', label: '常温' },
    { value: 'REFRIGERATED', label: '冷蔵' },
    { value: 'FROZEN', label: '冷凍' },
  ],

  productStorySectionTemplates: productStoryTemplateMock,
} satisfies SupplierProductFormOptions