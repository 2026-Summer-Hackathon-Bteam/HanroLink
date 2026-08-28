import { type ReactNode } from 'react'

type FormRowProps = {
  label: string
  htmlFor?: string
  children: ReactNode
}

function FormRow({ label, htmlFor, children }: FormRowProps) {
  const labelClassName = 'flex items-center bg-textbg px-5 py-4 md:border-r md:border-border text-left'
  return (
    <div className="grid md:grid-cols-[16rem_1fr]">
      {htmlFor ? (
        <label
          htmlFor={htmlFor}
          className={labelClassName}
        >
          {label}
        </label>
      ) : (
        <div className={labelClassName}>
          {label}
        </div>
      )}

      <div className="p-3 flex gap-4 items-center min-w-0">{children}</div>
    </div>
  )
}

export default FormRow
