package com.hanrolink.product.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.hanrolink.account.repository.BusinessUserAccountRepository;
import com.hanrolink.product.entity.MonthlySupplyCapacity;
import com.hanrolink.product.entity.Product;
import com.hanrolink.product.entity.ProductStory;
import com.hanrolink.product.repository.MonthlySupplyCapacityRepository;
import com.hanrolink.product.repository.ProductRepository;
import com.hanrolink.product.repository.ProductStoryRepository;
import com.hanrolink.product.request.SupplierProductCreateRequest;
import com.hanrolink.product.response.SupplierProductCreateResponse;

@Service
public class SupplierProductManagementService {

  private final BusinessUserAccountRepository businessUserAccountRepository;

  private final ProductRepository productRepository;

  private final MonthlySupplyCapacityRepository monthlySupplyCapacityRepository;

  private final ProductStoryRepository productStoryRepository;

  public SupplierProductManagementService(
    BusinessUserAccountRepository businessUserAccountRepository,
    ProductRepository productRepository,
    MonthlySupplyCapacityRepository monthlySupplyCapacityRepository,
    ProductStoryRepository productStoryRepository
  ) {
    this.businessUserAccountRepository = businessUserAccountRepository;
    this.productRepository = productRepository;
    this.monthlySupplyCapacityRepository = monthlySupplyCapacityRepository;
    this.productStoryRepository = productStoryRepository;
  }

  /**
   * 商品情報を新規作成する
   * @param identityProviderSubject 認証プロバイダーのユーザー識別子
   * @param request 商品の入力情報
   * @return 商品の作成結果
   */
  @Transactional
  public SupplierProductCreateResponse create(
    String identityProviderSubject,
    SupplierProductCreateRequest request
  ) {
    // TODO: S3連携時に、商品画像の保存キー生成とアップロード処理を追加する
    String mainImageStorageKey = "dummy/product-main-images/" + UUID.randomUUID();

    Long supplierAccountId = businessUserAccountRepository
      .findIdByIdentityProviderSubject(identityProviderSubject)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    Product product = new Product(
      supplierAccountId,
      request.productCategoryId(),
      request.mainIngredientRegionId(),
      request.name(),
      mainImageStorageKey,
      request.contentQuantity(),
      request.expirationType(),
      request.shelfLifeDays(),
      request.storageType(),
      request.desiredRetailPrice(),
      request.allergyInformation(),
      request.certificationInformation(),
      request.caseSize(),
      request.unitsPerCase(),
      request.minimumOrderQuantity(),
      request.shippingLeadTimeDays(),
      request.salesAreaRestriction()
    );

    Product savedProduct = productRepository.save(product);

    List<MonthlySupplyCapacity> monthlySupplyCapacities =
      request.monthlySupplyCapacities()
        .stream()
        .map(monthlySupplyCapacityRequest ->
          new MonthlySupplyCapacity(
            savedProduct.getId(),
            monthlySupplyCapacityRequest.targetMonth(),
            monthlySupplyCapacityRequest.availableQuantity()
          )
        )
        .toList();

    monthlySupplyCapacityRepository.saveAll(monthlySupplyCapacities);

    List<ProductStory> productStories =
      request.productStories()
        .stream()
        .map(productStoryRequest ->
          new ProductStory(
            savedProduct.getId(),
            productStoryRequest.productStorySectionTemplateId(),
            productStoryRequest.position(),
            productStoryRequest.body(),
            // TODO: S3連携時に、商品画像の保存キー生成とアップロード処理を追加する
            "dummy/product-story-images/" + UUID.randomUUID()
          )
        )
        .toList();

    productStoryRepository.saveAll(productStories);

    return new SupplierProductCreateResponse(
      savedProduct.getId()
    );
  }
}
