import { Outlet } from "react-router-dom";

function AuthenticatedPageLayout () {
    return (
        <div className="py-23">
            <Outlet />
        </div>
    )
}

export default AuthenticatedPageLayout