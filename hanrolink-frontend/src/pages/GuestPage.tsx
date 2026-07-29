import { Link } from 'react-router-dom'
import logo from '../assets/HanroLink_logo.png'
import mainVisual from '../assets/mainvisual.png'
import GuestCard from '../components/GuestCard'

function GuestPage() {
  return (
    <>
      <section className="relative h-100 overflow-hidden bg-bg max-w-300 mx-auto">
        <img
          src={mainVisual}
          alt="メインビジュアルのいちごジャム"
          className="absolute inset-y-0 right-0 h-full w-[73%] object-cover object-center"
        />

        <div
          aria-hidden="true"
          className="absolute inset-y-0 left-0 z-10 w-[55%] bg-linear-to-r from-bg via-bg/95 to-transparent"
        />

        <div className="relative z-20 flex h-full w-[42%] flex-col items-center justify-center gap-4 px-6">
          <img src={logo} alt="HanroLinkのロゴ" className="w-85" />

          <div className="flex gap-12">
            <Link
              to="/signup"
              className="flex h-9 w-30 items-center justify-center rounded-full border border-accent bg-accent text-bg"
            >
              新規登録
            </Link>

            <Link
              to="/login"
              className="flex h-9 w-30 items-center justify-center rounded-full border border-accent bg-bg text-accent"
            >
              ログイン
            </Link>
          </div>
        </div>
      </section>
      <div className="max-w-300 mx-auto">
        <p className='mb-8'>
          売り手と買い手のタイミングをマッチするHanroLinkへようこそ&#xFF01;&#xFF01;
        </p>
        <div className="mx-auto grid max-w-300 grid-cols-1 justify-items-center gap-7 px-4 md:grid-cols-2 xl:grid-cols-3">
          <GuestCard />
          <GuestCard />
          <GuestCard />
          <GuestCard />
          <GuestCard />
          <GuestCard />
          <GuestCard />
          <GuestCard />
          <GuestCard />
          <GuestCard />
          <GuestCard />
          <GuestCard />
          <GuestCard />
          <GuestCard />
          <GuestCard />
          <GuestCard />
          <GuestCard />
          <GuestCard />
          <GuestCard />
          <GuestCard />
        </div>
      </div>
    </>
  )
}

export default GuestPage
