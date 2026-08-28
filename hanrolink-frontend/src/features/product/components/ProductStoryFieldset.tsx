import FormRow from '../../../components/FormRow'
import type {
  StoryFormData,
  ProductStoryTemplate,
  StoryFormChanges,
  ProductFormMode,
} from '../productFormTypes'

type ProductStoryFieldsetProps = {
  mode: ProductFormMode
  story: StoryFormData
  templates: ProductStoryTemplate[]
  onChange: (position: number, changes: StoryFormChanges) => void
}

function ProductStoryFieldset({
  mode,
  story,
  templates,
  onChange,
}: ProductStoryFieldsetProps) {
  const selectedTemplate = templates.find(
    (temp) => temp.id === story.productStorySectionTemplateId,
  )

  return (
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

              onChange(story.position, {
                productStorySectionTemplateId:
                  value === '' ? '' : Number(value),
              })
            }}
            required
          >
            <option value="">選択してください</option>
            {[...templates]
              .sort((a, b) => a.id - b.id)
              .map((temp) => {
                return (
                  <option key={temp.id} value={temp.id}>
                    {temp.title}
                  </option>
                )
              })}
          </select>
        </FormRow>

        {story.existingImageUrl && (
          <FormRow label="現在登録されている画像">
            <img
              src={story.existingImageUrl}
              alt={`商品ストーリー${story.position}の現在の画像`}
              className="aspect-4/3 w-40 rounded-md object-cover"
            />
          </FormRow>
        )}
        <FormRow
          label={
            mode === 'create'
              ? `写真${story.position}`
              : `写真${story.position}を変更`
          }
          htmlFor={`image-${story.position}`}
        >
          <div className="w-full flex flex-col items-start gap-2 min-w-0">
            <div className="flex flex-col w-full items-start rounded-md p-2 bg-textbg">
              <p className="text-xs text-start">こんな写真をアップしよう</p>
              <p className="text-start">
                {selectedTemplate?.imageHint ??
                  'タイトルを選択すると入力補助が表示されます。'}
              </p>
            </div>
            <input
              id={`image-${story.position}`}
              name="imageFile"
              type="file"
              accept="image/png,image/jpeg,image/webp,image/heic,image/heif"
              className="file:text-bg file:rounded-full file:bg-border file:px-4 file:py-2 min-w-0"
              onChange={(e) => {
                onChange(story.position, {
                  imageFile: e.target.files?.[0] ?? null,
                })
              }}
              required={mode === 'create'}
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
                onChange(story.position, {
                  body: e.target.value,
                })
              }}
              required
            />
          </div>
        </FormRow>
      </div>
    </fieldset>
  )
}

export default ProductStoryFieldset
