import { Outlet } from "react-router-dom";

function MyPageRayout () {
    return (
        <div className="pt-23 pb-3 flex min-h-0 flex-1 flex-col">
            <Outlet />
        </div>
    )
}

export default MyPageRayout