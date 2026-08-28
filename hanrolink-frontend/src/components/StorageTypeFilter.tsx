type StorageType = {
  value: 'AMBIENT' | 'REFRIGERATED' | 'FROZEN'
  label: string
}

type StorageTypeFilterProps = {
  storageTypes: StorageType[]
  selectedStorageTypes: StorageType['value'][]
  onToggle: (storageType: StorageType['value']) => void
}

function StorageTypeFilter({
  storageTypes,
  selectedStorageTypes,
  onToggle,
}: StorageTypeFilterProps) {
  return (
    <div className="pb-4">
      <h3 className="mb-2 border-b-2 border-border px-2 text-left font-bold text-border textaccent">
        保存方法
      </h3>

      <div className="grid grid-cols-3 justify-items-start gap-3">
        {storageTypes.map((type) => (
          <label
            key={type.value}
            className="flex cursor-pointer items-center gap-1 rounded-md px-1 py-1.5 hover:bg-textbg/40"
          >
            <input
              type="checkbox"
              className="accent-border"
              value={type.value}
              checked={selectedStorageTypes.includes(type.value)}
              onChange={() => onToggle(type.value)}
            />
            <span>{type.label}</span>
          </label>
        ))}
      </div>
    </div>
  )
}

export default StorageTypeFilter
