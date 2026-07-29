import { Link } from 'react-router-dom'

type Role = 'supplier' | 'buyer'

const mypagePathByRole: Record<Role, string> = {
  supplier: '/mypage/supplier',
  buyer: '/mypage/buyer',
}

function SignupCompletePage() {
  // バックエンドから受け取ったデータをroleに代入する
  const role: Role = 'supplier'

  return (
    <>
      <div className="mx-auto max-w-300 px-4 text-center md:px-6 lg:px-8">
        <h2>新規登録申請完了</h2>
        <p className="mb-4 text-center">新規登録申請の受付が完了しました。</p>
        <p className="mb-4 text-center">
          現在、管理者による審査を行っております。
          <br />
          審査が完了するまで、今しばらくお待ちください。
        </p>
        <p className="mb-4 text-center">
          審査には通常<span className="text-xl text-accent">３日程度</span>
          お時間をいただきます。
          <br />
          なお、土日祝日をはさむ場合は、通常よりお時間をいただく場合があります。
        </p>
        <p className="mb-4 text-center">
          審査完了後、サービスをご利用いただけます。
        </p>
        <p className="mb-4 text-center">
          審査の過程で確認が必要となった場合は、ご入力いただいた電話番号またはメールアドレスへご連絡する場合があります。
          <br />
          その際は、ご対応くださいますようお願いいたします。
        </p>
        <p className="mt-16 text-center">
          マイページは<Link to={mypagePathByRole[role]}>こちら</Link>
          。（審査完了までコンテンツは表示されません）
        </p>
      </div>
    </>
  )
}

export default SignupCompletePage
