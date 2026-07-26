// ここでAPI通信の設定を1か所にまとめる
import createClient from "openapi-fetch";
import type { paths } from "../shared/api/schema";

export const api = createClient<paths>({
  baseUrl: import.meta.env.VITE_API_URL,
});