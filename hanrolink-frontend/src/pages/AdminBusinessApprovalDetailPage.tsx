import { useState, useEffect } from 'react'
import { useParams } from 'react-router-dom'
import DataRow from '../components/DataRow'
import type { AdminBusinessApprovalDetail } from '../features/admin/adminApprovalTypes'
import { getAdminBusinessApprovalDetail } from '../features/admin/adminApprovalService'

const roleInKana: Record<
  AdminBusinessApprovalDetail['businessUserAccount']['role'],
  string
> = {
  SUPPLIER: 'サプライヤー',
  BUYER: 'バイヤー',
}

const reviewStatusInJp: Record<
  AdminBusinessApprovalDetail['businessUserAccount']['reviewStatus'],
  string
> = {
  PENDING: '審査待ち',
  APPROVED: '審査済み',
}

function AdminBusinessApprovalDetailPage() {
  const [data, setData] = useState<AdminBusinessApprovalDetail | null>(null)
  const [error, setError] = useState('')
  const { businessUserAccountId } = useParams<{
    businessUserAccountId: string
  }>()

  useEffect(() => {
    let isCancelled = false

    const loadAdminApprovalData = async () => {
      try {
        if (!businessUserAccountId) return
        const result = await getAdminBusinessApprovalDetail(
          businessUserAccountId,
        )
        if (!isCancelled) {
          setData(result)
        }
      } catch {
        if (!isCancelled) {
          setError('新規登録者詳細の情報を取得できませんでした')
        }
      }
    }
    void loadAdminApprovalData()
    return () => {
      isCancelled = true
    }
  }, [businessUserAccountId])

  if (!businessUserAccountId) {
    return (
      <p role="alert" className="py-10 text-center text-error">
        新規登録者を特定できませんでした。
      </p>
    )
  }

  if (error) {
    return (
      <p role="alert" className="py-10 text-center text-error">
        {error}
      </p>
    )
  }

  if (!data) {
    return <p className="py-10 text-center">読み込み中...</p>
  }

  return (
    <div className="mx-auto max-w-300 px-4 text-center md:px-6 lg:px-8">
      <h2>新規登録者詳細</h2>
      <dl className="flex flex-col mx-auto overflow-hidden border border-border divide-y divide-border">
        <DataRow itemName="バイヤー／サプライヤー">
          {roleInKana[data.businessUserAccount.role]}
        </DataRow>
        <DataRow itemName="会社名">{data.business.name}</DataRow>
        <DataRow itemName="会社名フリガナ">{data.business.nameKana}</DataRow>
        <DataRow itemName="郵便番号">
          {data.business.addressPostalCode.slice(0, 3)}
          {'-'}
          {data.business.addressPostalCode.slice(3, 7)}
        </DataRow>
        <DataRow itemName="会社住所">
          {[
            data.business.addressPrefecture,
            data.business.addressMunicipalityStreet,
            data.business.addressBuilding,
          ]
            .filter(Boolean)
            .join('')}
        </DataRow>
        <DataRow itemName="会社電話番号">{data.business.phoneNumber}</DataRow>
        <DataRow itemName="ホームページ">
          {data.business.websiteUrl ? (
            <a
              href={data.business.websiteUrl}
              target="_blank"
              rel="noopener noreferrer"
              className="break-all text-other underline"
            >
              {data.business.websiteUrl}
            </a>
          ) : (
            '未登録'
          )}
        </DataRow>
        <DataRow itemName="担当者名">
          {data.businessUserAccount.lastName}{' '}
          {data.businessUserAccount.firstName}
        </DataRow>
        <DataRow itemName="担当者名フリガナ">
          {data.businessUserAccount.lastNameKana}{' '}
          {data.businessUserAccount.firstNameKana}
        </DataRow>
        <DataRow itemName="担当者電話番号">
          {data.businessUserAccount.phoneNumber}
        </DataRow>
        <DataRow itemName="メールアドレス">
          {data.businessUserAccount.email}
        </DataRow>
        <DataRow itemName="審査状態">
          {reviewStatusInJp[data.businessUserAccount.reviewStatus]}
        </DataRow>
        <DataRow itemName="登録日時">
          {new Date(data.businessUserAccount.createdAt).toLocaleString('ja-JP')}
        </DataRow>
      </dl>
      <button
        type="button"
        className="h-9 w-45 mx-auto mt-8 rounded-full border border-accent bg-accentbg"
      >
        承認
        {/* （idは承認ボタンを押した時にアクセスするAPIのパスに使う） */}
      </button>
    </div>
  )
}

export default AdminBusinessApprovalDetailPage
