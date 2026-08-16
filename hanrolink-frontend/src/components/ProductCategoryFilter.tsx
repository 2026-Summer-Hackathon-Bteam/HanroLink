type ProductCategoryGroup = {
  id: number
  name: string
}

type ProductCategory = {
  id: number
  productCategoryGroupId: number
  name: string
}

type ProductCategoryFilterProps = {
  categoryGroups: ProductCategoryGroup[]
  categories: ProductCategory[]
  selectedCategoryIds: number[]
  onToggle: (categoryId: number) => void
}

function ProductCategoryFilter({
  categoryGroups,
  categories,
  selectedCategoryIds,
  onToggle,
}: ProductCategoryFilterProps) {
  return (
    <div className="pb-4">
      <h3 className="mb-2 border-b-2 border-border px-2 text-left font-bold text-border textaccent">
        商品カテゴリー
      </h3>
      {[...categoryGroups]
        .sort((a, b) => a.id - b.id)
        .map((group) => (
          <details className="group rounded-lg text-left" key={group.id}>
            <summary className="cursor-pointer rounded-md px-2 py-2 font-medium hover:bg-textbg/40">
              {group.name}
            </summary>
            {[...categories]
              .filter(
                (category) => group.id === category.productCategoryGroupId,
              )
              .sort((a, b) => a.id - b.id)
              .map((category) => (
                <div
                  key={category.id}
                  className="ml-3 border-l border-border/30 pl-2"
                >
                  <label>
                    <input
                      type="checkbox"
                      value={category.id}
                      checked={selectedCategoryIds.includes(category.id)}
                      onChange={() => onToggle(category.id)}
                    />
                    <span>{category.name}</span>
                  </label>
                </div>
              ))}
          </details>
        ))}
    </div>
  )
}

export default ProductCategoryFilter
