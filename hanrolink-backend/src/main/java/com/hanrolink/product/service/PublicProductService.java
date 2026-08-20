package com.hanrolink.product.service;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanrolink.infrastructure.s3.S3DownloadUrlGenerator;
import com.hanrolink.product.repository.ProductRepository;
import com.hanrolink.product.response.PublicProductListResponse;

@Profile("s3")
@Service
public class PublicProductService {

  private static final int PRODUCT_LIST_LIMIT = 20;

  private final ProductRepository productRepository;

  private final S3DownloadUrlGenerator s3DownloadUrlGenerator;

  public PublicProductService(
    ProductRepository productRepository,
    S3DownloadUrlGenerator s3DownloadUrlGenerator
  ) {
    this.productRepository = productRepository;
    this.s3DownloadUrlGenerator = s3DownloadUrlGenerator;
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
          s3DownloadUrlGenerator.generate(product.mainImageStorageKey())
        )
      )
      .toList();
  }
}
