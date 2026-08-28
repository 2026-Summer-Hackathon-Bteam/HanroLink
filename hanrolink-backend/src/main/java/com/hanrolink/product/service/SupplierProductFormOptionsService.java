package com.hanrolink.product.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanrolink.product.enums.ProductExpirationType;
import com.hanrolink.product.enums.StorageType;
import com.hanrolink.product.repository.ProductStorySectionTemplateRepository;
import com.hanrolink.product.response.SupplierProductFormOptionsResponse;
import com.hanrolink.product.response.component.ProductExpirationTypeOptionResponse;
import com.hanrolink.product.response.component.ProductStorySectionTemplateOptionResponse;
import com.hanrolink.product.response.component.StorageTypeOptionResponse;
import com.hanrolink.productcategory.repository.ProductCategoryGroupRepository;
import com.hanrolink.productcategory.repository.ProductCategoryRepository;
import com.hanrolink.productcategory.response.component.ProductCategoryGroupOptionResponse;
import com.hanrolink.productcategory.response.component.ProductCategoryOptionResponse;
import com.hanrolink.region.repository.PrefectureRepository;
import com.hanrolink.region.response.component.PrefectureOptionResponse;

@Service
public class SupplierProductFormOptionsService {

  private final ProductCategoryGroupRepository productCategoryGroupRepository;

  private final ProductCategoryRepository productCategoryRepository;

  private final PrefectureRepository prefectureRepository;

  private final ProductStorySectionTemplateRepository productStorySectionTemplateRepository;

  public SupplierProductFormOptionsService(
    ProductCategoryGroupRepository productCategoryGroupRepository,
    ProductCategoryRepository productCategoryRepository,
    PrefectureRepository prefectureRepository,
    ProductStorySectionTemplateRepository productStorySectionTemplateRepository
  ) {
    this.productCategoryGroupRepository = productCategoryGroupRepository;
    this.productCategoryRepository = productCategoryRepository;
    this.prefectureRepository = prefectureRepository;
    this.productStorySectionTemplateRepository = productStorySectionTemplateRepository;
  }

  /**
   * 商品情報入力フォームで使用する選択肢を取得する
   * @return 商品情報入力フォームの選択肢
   */
  @Transactional(readOnly = true)
  public SupplierProductFormOptionsResponse get() {
    List<ProductCategoryGroupOptionResponse> productCategoryGroups =
      productCategoryGroupRepository
        .findAllOptions()
        .stream()
        .map(productCategoryGroup ->
          new ProductCategoryGroupOptionResponse(
            productCategoryGroup.id(),
            productCategoryGroup.name()
          )
        )
        .toList();

    List<ProductCategoryOptionResponse> productCategories =
      productCategoryRepository
        .findAllOptions()
        .stream()
        .map(productCategory ->
          new ProductCategoryOptionResponse(
            productCategory.id(),
            productCategory.productCategoryGroupId(),
            productCategory.name()
          )
        )
        .toList();

    List<PrefectureOptionResponse> mainIngredientOriginPrefectures =
      prefectureRepository
        .findAllOptions()
        .stream()
        .map(prefecture ->
          new PrefectureOptionResponse(
            prefecture.id(),
            prefecture.name()
          )
        )
        .toList();

    List<ProductExpirationTypeOptionResponse> productExpirationTypes =
      Arrays.stream(ProductExpirationType.values())
        .map(productExpirationType ->
          new ProductExpirationTypeOptionResponse(
            productExpirationType,
            productExpirationType.getDisplayName()
          )
        )
        .toList();

    List<StorageTypeOptionResponse> storageTypes =
      Arrays.stream(StorageType.values())
        .map(storageType ->
          new StorageTypeOptionResponse(
            storageType,
            storageType.getDisplayName()
          )
        )
        .toList();

    List<ProductStorySectionTemplateOptionResponse> productStorySectionTemplates =
      productStorySectionTemplateRepository
        .findAllOptions()
        .stream()
        .map(productStorySectionTemplate ->
          new ProductStorySectionTemplateOptionResponse(
            productStorySectionTemplate.id(),
            productStorySectionTemplate.title(),
            productStorySectionTemplate.imageHint(),
            productStorySectionTemplate.bodyHelpText(),
            productStorySectionTemplate.bodyExample()
          )
        )
        .toList();

    return new SupplierProductFormOptionsResponse(
      productCategoryGroups,
      productCategories,
      mainIngredientOriginPrefectures,
      productExpirationTypes,
      storageTypes,
      productStorySectionTemplates
    );
  }
}
