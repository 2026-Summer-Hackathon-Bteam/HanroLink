package com.hanrolink.negotiationrequest.service;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanrolink.infrastructure.s3.S3DownloadUrlGenerator;
import com.hanrolink.negotiationrequest.response.SupplierNegotiationRequestSelectableProductResponse;
import com.hanrolink.product.repository.ProductRepository;

@Profile("s3")
@Service
public class SupplierNegotiationRequestSelectableProductService {

  private final ProductRepository productRepository;

  private final S3DownloadUrlGenerator s3DownloadUrlGenerator;

  public SupplierNegotiationRequestSelectableProductService(
    ProductRepository productRepository,
    S3DownloadUrlGenerator s3DownloadUrlGenerator
  ) {
    this.productRepository = productRepository;
    this.s3DownloadUrlGenerator = s3DownloadUrlGenerator;
  }

  /**
   * 募集への商談希望で選択可能な自社商品一覧を取得する
   * @param identityProviderSubject 認証プロバイダーのユーザー識別子
   * @return 選択可能な自社商品一覧
   */
  @Transactional(readOnly = true)
  public List<SupplierNegotiationRequestSelectableProductResponse> list(
    String identityProviderSubject
  ) {
    return productRepository
      .findSelectableProductsForNegotiationRequest(identityProviderSubject)
      .stream()
      .map(product ->
        new SupplierNegotiationRequestSelectableProductResponse(
          product.publicId(),
          product.name(),
          s3DownloadUrlGenerator.generate(product.mainImageStorageKey())
        )
      )
      .toList();
  }
}
