package com.hanrolink.product.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanrolink.product.repository.ProductRepository;
import com.hanrolink.product.response.PublicProductListResponse;

@Service
public class PublicProductService {

  private static final int PRODUCT_LIST_LIMIT = 20;

  private final ProductRepository productRepository;

  public PublicProductService(
    ProductRepository productRepository
  ) {
    this.productRepository = productRepository;
  }

  /**
   * ゲスト向け画面に掲載する商品一覧を取得する
   * @return ゲスト向け画面に掲載する商品一覧
   */
  @Transactional(readOnly = true)
  public List<PublicProductListResponse> list() {
    return productRepository
      .findPublicListItems(Pageable.ofSize(PRODUCT_LIST_LIMIT))
      .stream()
      .map(product ->
        new PublicProductListResponse(
          product.name(),
          product.supplierBusinessName(),
          // TODO: S3連携時にストレージキーを署名付きURLへ変換する
          "dummy/" + product.mainImageStorageKey()
        )
      )
      .toList();
  }
}
