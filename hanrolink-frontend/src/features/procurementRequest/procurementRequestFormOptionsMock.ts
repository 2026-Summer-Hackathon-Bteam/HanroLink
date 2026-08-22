import type { ProcurementRequestFormOptions } from './procurementRequestFormTypes'

export const procurementRequestFormOptionsMock = {
  productCategoryGroups: [
    {
      id: 1,
      name: '食品',
    },
    {
      id: 2,
      name: 'スイーツ・お菓子',
    },
    {
      id: 3,
      name: 'お酒',
    },
    {
      id: 4,
      name: '水・ソフトドリンク',
    },
    {
      id: 5,
      name: 'その他',
    },
  ],

  productCategories: [
    {
      id: 1,
      productCategoryGroupId: 1,
      name: '米・雑穀・シリアル',
    },
    {
      id: 3,
      productCategoryGroupId: 1,
      name: '野菜',
    },
    {
      id: 7,
      productCategoryGroupId: 1,
      name: '果物',
    },
    {
      id: 11,
      productCategoryGroupId: 1,
      name: 'ジャム・はちみつ',
    },
    {
      id: 17,
      productCategoryGroupId: 2,
      name: '洋菓子',
    },
    {
      id: 18,
      productCategoryGroupId: 2,
      name: '和菓子',
    },
    {
      id: 23,
      productCategoryGroupId: 3,
      name: 'ビール・地ビール',
    },
    {
      id: 31,
      productCategoryGroupId: 4,
      name: 'ソフトドリンク',
    },
    {
      id: 38,
      productCategoryGroupId: 5,
      name: 'その他',
    },
  ],

  storageTypes: [
    {
      value: 'AMBIENT',
      label: '常温',
    },
    {
      value: 'REFRIGERATED',
      label: '冷蔵',
    },
    {
      value: 'FROZEN',
      label: '冷凍',
    },
  ],
} satisfies ProcurementRequestFormOptions