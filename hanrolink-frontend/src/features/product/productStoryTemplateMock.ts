import type { components } from '../../shared/api/schema'

type ProductStoryTemplate =
  components['schemas']['ProductStorySectionTemplateOptionResponse']

export const productStoryTemplateMock = [
  {
    id: 1,
    title: '素材・原料',
    imageHint:
      '原材料そのもの、畑、果樹園、牧場、収穫前の素材、生産現場の写真',
    bodyHelpText: `この商品に使っている素材や原料について教えてください。
      産地、品種、収穫時期、鮮度、栽培方法など、素材の良さが伝わる内容があれば入力してください。`,
    bodyExample:
      '青森県産のりんごを使用しています。寒暖差のある地域で育ったりんごを、香りと酸味のバランスが良い時期に収穫しています。',
    sortOrder: 1,
  },
  {
    id: 2,
    title: '産地・地域性',
    imageHint: '地域の風景、農園、港、山、町並み、地域名が伝わる看板や背景',
    bodyHelpText: `この商品が生まれた地域や、地域ならではの特徴について教えてください。
      地域の気候、文化、地元食材、地域フェアで伝えやすい背景などがあれば入力してください`,
    bodyExample: '地元で長く親しまれている果物を使った商品です。地域フェアや観光向けの売場で、産地の魅力を伝えやすい商品です。',
    sortOrder: 2,
  },
  {
    id: 3,
    title: '作り手の想い',
    imageHint:
      '作り手本人、作業中の様子、工房、家族やスタッフ、商品を手に持っている写真',
    bodyHelpText: `この商品を作っている人の想いや、商品づくりで大切にしていることを教えてください。
なぜこの商品を作ったのか、どんな人に届けたいのかなどを書いてください。`,
    bodyExample:
      '地元の果物のおいしさを、季節を問わず楽しんでもらいたいという想いから作りました。素材の味を残すことを大切にしています。',
    sortOrder: 3,
  },
] satisfies ProductStoryTemplate[]
