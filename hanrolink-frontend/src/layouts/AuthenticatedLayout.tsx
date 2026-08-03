import { Outlet } from "react-router-dom";
import Footer from "../components/Footer";
import type { CurrentAccount } from "../features/auth/authRouting";
import Header from "../components/Header";

function AuthenticatedLayout() {
    const temporaryAccount: CurrentAccount = {
        // 仮の値を設定
        role: 'SUPPLIER',
        businessUserAccountRegistrationStatus: 'APPROVED',
    }

    return (
        <div className="flex min-h-dvh flex-col">
            <Header isLoggedIn={true} account={temporaryAccount}/>
            <main className="py-23 flex-1">
                <Outlet />
            </main>
            <Footer />
        </div>
    )
}

export default AuthenticatedLayout