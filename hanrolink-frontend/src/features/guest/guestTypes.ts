import type { components } from "../../shared/api/schema"

type GuestProduct = components['schemas']['PublicProductListResponse']

export type GuestData = GuestProduct[]
