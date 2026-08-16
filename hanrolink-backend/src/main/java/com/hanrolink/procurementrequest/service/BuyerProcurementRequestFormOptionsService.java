package com.hanrolink.procurementrequest.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanrolink.procurementrequest.response.BuyerProcurementRequestFormOptionsResponse;
import com.hanrolink.product.enums.StorageType;
import com.hanrolink.product.response.component.StorageTypeOptionResponse;
import com.hanrolink.productcategory.repository.ProductCategoryGroupRepository;
import com.hanrolink.productcategory.repository.ProductCategoryRepository;
import com.hanrolink.productcategory.response.component.ProductCategoryGroupOptionResponse;
import com.hanrolink.productcategory.response.component.ProductCategoryOptionResponse;

@Service
public class BuyerProcurementRequestFormOptionsService {

  private final ProductCategoryGroupRepository productCategoryGroupRepository;

  private final ProductCategoryRepository productCategoryRepository;

  public BuyerProcurementRequestFormOptionsService(
    ProductCategoryGroupRepository productCategoryGroupRepository,
    ProductCategoryRepository productCategoryRepository
  ) {
    this.productCategoryGroupRepository = productCategoryGroupRepository;
    this.productCategoryRepository = productCategoryRepository;
  }

  /**
   * 募集情報入力フォームで使用する選択肢を取得する
   * @return 募集情報入力フォームの選択肢
   */
  @Transactional(readOnly = true)
  public BuyerProcurementRequestFormOptionsResponse get() {
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

    List<StorageTypeOptionResponse> storageTypes =
      Arrays.stream(StorageType.values())
        .map(storageType ->
          new StorageTypeOptionResponse(
            storageType,
            storageType.getDisplayName()
          )
        )
        .toList();

    return new BuyerProcurementRequestFormOptionsResponse(
      productCategoryGroups,
      productCategories,
      storageTypes
    );
  }
}
