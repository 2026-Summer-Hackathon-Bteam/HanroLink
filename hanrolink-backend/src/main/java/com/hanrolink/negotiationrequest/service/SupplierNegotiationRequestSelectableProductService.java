package com.hanrolink.negotiationrequest.service;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanrolink.infrastructure.cloudfront.CloudFrontDownloadUrlGenerator;
import com.hanrolink.negotiationrequest.response.SupplierNegotiationRequestSelectableProductResponse;
import com.hanrolink.product.repository.ProductRepository;

@Profile("cloudfront")
@Service
public class SupplierNegotiationRequestSelectableProductService {

  private final ProductRepository productRepository;

  private final CloudFrontDownloadUrlGenerator cloudFrontDownloadUrlGenerator;

  public SupplierNegotiationRequestSelectableProductService(
    ProductRepository productRepository,
    CloudFrontDownloadUrlGenerator cloudFrontDownloadUrlGenerator
  ) {
    this.productRepository = productRepository;
    this.cloudFrontDownloadUrlGenerator = cloudFrontDownloadUrlGenerator;
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
          cloudFrontDownloadUrlGenerator.generate(product.mainImageStorageKey())
        )
      )
      .toList();
  }
}
