export function createTargetMonths(
  currentDate = new Date(),
  length = 6,
): string[] {
  return Array.from({ length }, (_, index) => {
    const targetDate = new Date(
      currentDate.getFullYear(),
      currentDate.getMonth() + index,
      1,
    )

    const year = targetDate.getFullYear()
    const month = String(targetDate.getMonth() + 1).padStart(2, '0')

    return `${year}-${month}`
  })
}

export function formatTargetMonth(targetMonth: string): string {
  const [year, month] = targetMonth.split('-')

  return `${year}年${Number(month)}月`
}
