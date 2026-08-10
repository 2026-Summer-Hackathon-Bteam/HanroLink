import type { ProcurementRequestFormOptions } from "./procurementRequestFormTypes";
import { procurementRequestFormOptionsMock } from "./procurementRequestFormOptionsMock";

export function getProcurementRequestFormOptions(): Promise<ProcurementRequestFormOptions> {
    return Promise.resolve(procurementRequestFormOptionsMock)
} 