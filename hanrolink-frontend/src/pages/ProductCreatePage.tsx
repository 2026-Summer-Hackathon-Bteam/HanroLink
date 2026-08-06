import { useState } from 'react'
import FormRow from '../components/FormRow'
import { productStoryTemplateMock } from '../features/product/productStoryTemplateMock'

type StoryFormData = {
  position: number
  productStorySectionTemplateId: number | ''
  imageFile: File | null
  body: string
}

function ProductCreatePage() {
  const [story, setStory] = useState<StoryFormData>({
    position: 1,
    productStorySectionTemplateId: '',
    imageFile: null,
    body: '',
  })

  const selectedTemplate = productStoryTemplateMock.find(
    (temp) => temp.id === story.productStorySectionTemplateId,
  )

  return (
    <div className="mx-auto max-w-300 px-4 text-center md:px-6 lg:px-8">
      <h2>商品登録</h2>
      <p className="mb-8">
        商品情報と一緒に４枚の写真を使って、バイヤーにアピールしましょう！
        <br />
        タイトルを選択すると写真や文章のヒントが表示されます
        <br />
        商品の魅力に合うものを選んで、商品のこだわりを伝えてください。
      </p>
      <form className="flex flex-col mx-auto">
        <h3 className="text-start pl-1">商品ストーリー</h3>
        <div>
          <fieldset className="m-0 min-w-0 w-full p-0 border-0">
            <legend className="w-full px-5 py-1 text-left border border-b-0 border-border bg-textbg">
              {`商品ストーリー${story.position}`}
            </legend>
            <div className="overflow-hidden border border-border divide-y divide-border">
              <FormRow
                label={`タイトル${story.position}`}
                htmlFor={`title-${story.position}`}
              >
                <select
                  id={`title-${story.position}`}
                  name="productStorySectionTemplateId"
                  className="h-11 w-full md:w-1/3 rounded-lg border-[0.5px] border-text px-3 shadow-sm"
                  value={story.productStorySectionTemplateId}
                  onChange={(e) => {
                    const value = e.target.value

                    setStory((prev) => ({
                      ...prev,
                      productStorySectionTemplateId:
                        value === '' ? '' : Number(value),
                    }))
                  }}
                >
                  <option value="">選択してください</option>
                  {[...productStoryTemplateMock]
                    .sort((a, b) => a.sortOrder - b.sortOrder)
                    .map((temp) => {
                      return (
                        <option key={temp.id} value={temp.id}>
                          {temp.title}
                        </option>
                      )
                    })}
                </select>
              </FormRow>

              <FormRow
                label={`写真${story.position}`}
                htmlFor={`image-${story.position}`}
              >
                <div className="w-full flex flex-col items-start gap-2">
                  <div className="flex flex-col w-full items-start rounded-md p-2 bg-textbg">
                    <p className="text-xs text-start">
                      こんな写真をアップしよう
                    </p>
                    <p className="text-start">
                      {selectedTemplate?.imageHint ??
                        'タイトルを選択すると入力補助が表示されます。'}
                    </p>
                  </div>
                  <input
                    id={`image-${story.position}`}
                    name="imageFile"
                    type="file"
                    accept="image/png,image/jpeg,image/webp"
                    className="file:text-bg file:rounded-full file:bg-border file:px-4 file:py-2"
                    onChange={(e) => {
                      setStory((prev) => ({
                        ...prev,
                        imageFile: e.target.files?.[0] ?? null,
                      }))
                    }}
                  />
                </div>
              </FormRow>
              <FormRow
                label={`文章${story.position}`}
                htmlFor={`body-${story.position}`}
              >
                <div className="w-full flex flex-col items-start gap-2">
                  <div className="flex flex-col w-full items-start rounded-md p-2 bg-textbg">
                    <p className="text-xs text-start">こんな文章を考えよう！</p>
                    <p className="text-start">
                      {selectedTemplate?.bodyHelpText ??
                        'タイトルを選択すると入力補助が表示されます。'}
                    </p>
                  </div>
                  <textarea
                    id={`body-${story.position}`}
                    className="w-full p-2 h-30 md:h-auto"
                    placeholder={`${selectedTemplate?.bodyExample ?? 'タイトルを選択すると入力補助が表示されます。'}（文字数上限：255文字）`}
                    name="body"
                    maxLength={255}
                    value={story.body}
                    onChange={(e) => {
                      setStory((prev) => ({
                        ...prev,
                        body: e.target.value,
                      }))
                    }}
                  />
                </div>
              </FormRow>
            </div>
          </fieldset>
        </div>
      </form>
    </div>
  )
}

export default ProductCreatePage
