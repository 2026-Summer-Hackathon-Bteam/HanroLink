import { useState, useEffect } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import DataRow from '../components/DataRow'
import { getBuyerProfile } from '../features/buyer/buyerProfileService'
import type { BuyerProfile } from '../features/buyer/buyerProfileTypes'

function BuyerProfilePage() {
  const [buyerProfile, setBuyerProfile] = useState<BuyerProfile | null>(null)
  const navigate = useNavigate()
  const { businessId } = useParams<{
    businessId: string
  }>()
  const [error, setError] = useState('')

  useEffect(() => {
    let isCancelled = false

    const loadBuyerProfile = async () => {
      if (!businessId) return
      try {
        const profile = await getBuyerProfile(businessId)

        if (!isCancelled) {
          setBuyerProfile(profile)
        }
      } catch(error) {
        if (!isCancelled) {
          setError(error instanceof Error ? error.message :'バイヤー情報を取得できませんでした。')
        }
      }
    }

    void loadBuyerProfile()

    return () => {
      isCancelled = true
    }
  }, [businessId])

  if (!businessId) {
    return (
      <p role="alert" className="py-10 text-center text-error">
        バイヤーを特定できませんでした。
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

  if (!buyerProfile) {
    return <p className="py-10 text-center">読み込み中...</p>
  }

  return (
    <div className="mx-auto max-w-300 px-4 text-center md:px-6 lg:px-8">
      <h2>バイヤー情報</h2>
      <dl className="flex flex-col mx-auto overflow-hidden border border-border divide-y divide-border">
        <DataRow itemName="会社名">{buyerProfile.businessName}</DataRow>
        <DataRow itemName="会社住所">
          {[
            buyerProfile.businessAddressPrefecture,
            buyerProfile.businessAddressMunicipalityStreet,
            buyerProfile.businessAddressBuilding,
          ]
            .filter(Boolean)
            .join('')}
        </DataRow>
        <DataRow itemName="ホームページ">
          {buyerProfile.websiteUrl ? (
            <a
              href={buyerProfile.websiteUrl}
              target="_blank"
              rel="noopener noreferrer"
              className="text-other underline"
            >
              {buyerProfile.websiteUrl}
            </a>
          ) : (
            '-'
          )}
        </DataRow>
      </dl>
      <button
        type="button"
        onClick={() => navigate(-1)}
        className="mx-auto mt-16 h-9 w-45 rounded-full border border-accent bg-accentbg"
      >
        前のページに戻る
      </button>
    </div>
  )
}

export default BuyerProfilePage
