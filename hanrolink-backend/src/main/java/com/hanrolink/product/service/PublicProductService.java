package com.hanrolink.product.service;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanrolink.infrastructure.cloudfront.CloudFrontDownloadUrlGenerator;
import com.hanrolink.product.repository.ProductRepository;
import com.hanrolink.product.response.PublicProductListResponse;

@Profile("cloudfront")
@Service
public class PublicProductService {

  private static final int PRODUCT_LIST_LIMIT = 20;

  private final ProductRepository productRepository;

  private final CloudFrontDownloadUrlGenerator cloudFrontDownloadUrlGenerator;

  public PublicProductService(
    ProductRepository productRepository,
    CloudFrontDownloadUrlGenerator cloudFrontDownloadUrlGenerator
  ) {
    this.productRepository = productRepository;
    this.cloudFrontDownloadUrlGenerator = cloudFrontDownloadUrlGenerator;
  }

  /**
   * ゲスト向け画面に掲載する商品一覧を取得する
   * @return ゲスト向け画面に掲載する商品一覧
   */
  @Transactional(readOnly = true)
  public List<PublicProductListResponse> list() {
    return productRepository
      .findPublicList(Pageable.ofSize(PRODUCT_LIST_LIMIT))
      .stream()
      .map(product ->
        new PublicProductListResponse(
          product.publicId(),
          product.name(),
          product.supplierBusinessName(),
          cloudFrontDownloadUrlGenerator.generate(product.mainImageStorageKey())
        )
      )
      .toList();
  }
}
