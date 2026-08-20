package com.hanrolink.product.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanrolink.product.enums.StorageType;
import com.hanrolink.product.response.ProductSearchOptionsResponse;
import com.hanrolink.product.response.component.StorageTypeOptionResponse;
import com.hanrolink.productcategory.repository.ProductCategoryGroupRepository;
import com.hanrolink.productcategory.repository.ProductCategoryRepository;
import com.hanrolink.productcategory.response.component.ProductCategoryGroupOptionResponse;
import com.hanrolink.productcategory.response.component.ProductCategoryOptionResponse;
import com.hanrolink.region.repository.RegionRepository;
import com.hanrolink.region.response.component.RegionOptionResponse;

@Service
public class ProductSearchOptionsService {

  private final ProductCategoryGroupRepository productCategoryGroupRepository;

  private final ProductCategoryRepository productCategoryRepository;

  private final RegionRepository regionRepository;

  public ProductSearchOptionsService(
    ProductCategoryGroupRepository productCategoryGroupRepository,
    ProductCategoryRepository productCategoryRepository,
    RegionRepository regionRepository
  ) {
    this.productCategoryGroupRepository = productCategoryGroupRepository;
    this.productCategoryRepository = productCategoryRepository;
    this.regionRepository = regionRepository;
  }

  /**
   * 商品検索フォームで使用する選択肢を取得する
   * @return 商品検索フォームの選択肢
   */
  @Transactional(readOnly = true)
  public ProductSearchOptionsResponse get() {
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

    List<RegionOptionResponse> mainIngredientOriginRegions =
      regionRepository
        .findAllOptions()
        .stream()
        .map(region ->
          new RegionOptionResponse(
            region.id(),
            region.name()
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

    return new ProductSearchOptionsResponse(
      productCategoryGroups,
      productCategories,
      mainIngredientOriginRegions,
      storageTypes
    );
  }
}
