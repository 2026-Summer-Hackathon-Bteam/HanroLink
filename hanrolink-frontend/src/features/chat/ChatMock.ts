import type {
  ChatDetail,
  ChatMessages,
} from './ChatTypes'

export const chatDetailMock: ChatDetail = {
  name: '国産いちごジャム／いちご加工品の募集',
  counterpartyBusinessName: '株式会社サンプルフーズ',
}

export const chatMessagesMock: ChatMessages = [
  {
    id: 1,
    senderBusinessName: '株式会社サンプルフーズ',
    isMine: false,
    body: '商談希望を承認いただき、ありがとうございます。商品の詳細について確認させてください。',
    createdAt: '2026-08-15T09:30:00+09:00',
    messageFiles: [],
  },
  {
    id: 2,
    senderBusinessName: '株式会社はんろ農園',
    isMine: true,
    body: 'お問い合わせありがとうございます。確認したい内容をお送りください。',
    createdAt: '2026-08-15T09:35:00+09:00',
    messageFiles: [],
  },
  {
    id: 3,
    senderBusinessName: '株式会社サンプルフーズ',
    isMine: false,
    body: '希望する納品条件を添付しましたので、ご確認をお願いします。',
    createdAt: '2026-08-15T10:02:00+09:00',
    messageFiles: [
      {
        displayFilename: '納品条件書_2026年8月版.pdf',
        url: '/mock-files/delivery-conditions.pdf',
        mimeType: 'application/pdf',
        fileSizeBytes: 245760,
      },
    ],
  },
  {
    id: 4,
    senderBusinessName: '株式会社はんろ農園',
    isMine: true,
    createdAt: '2026-08-15T10:15:00+09:00',
    messageFiles: [
      {
        displayFilename: '商品写真_正面.png',
        url: '/mock-files/product-front.png',
        mimeType: 'image/png',
        fileSizeBytes: 524288,
      },
      {
        displayFilename: '商品規格書.xlsx',
        url: '/mock-files/product-specification.xlsx',
        mimeType:
          'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        fileSizeBytes: 86016,
      },
    ],
  },
]