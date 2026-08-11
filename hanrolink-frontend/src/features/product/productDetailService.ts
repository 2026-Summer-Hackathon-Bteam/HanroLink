import type { ProductDetail } from "./productDetailTypes";
import { productDetailMock } from "./productDetailMock";

export function getProductDetailData(productId :number):Promise<ProductDetail> {
    if(productId !== 1) return Promise.reject('データがありません。')
    return Promise.resolve(productDetailMock)
}