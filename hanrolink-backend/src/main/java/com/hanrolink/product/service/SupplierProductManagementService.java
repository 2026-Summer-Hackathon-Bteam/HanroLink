package com.hanrolink.product.service;

import java.time.Instant;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.hanrolink.account.repository.BusinessUserAccountRepository;
import com.hanrolink.file.entity.PendingFileUpload;
import com.hanrolink.file.enums.FileUploadUsage;
import com.hanrolink.file.policy.PendingFileUploadPolicy;
import com.hanrolink.file.repository.PendingFileUploadRepository;
import com.hanrolink.file.service.PendingFileDeletionService;
import com.hanrolink.infrastructure.cloudfront.CloudFrontDownloadUrlGenerator;
import com.hanrolink.infrastructure.s3.S3UploadedFileVerifier;
import com.hanrolink.product.entity.MonthlySupplyCapacity;
import com.hanrolink.product.entity.Product;
import com.hanrolink.product.entity.ProductStory;
import com.hanrolink.product.repository.MonthlySupplyCapacityRepository;
import com.hanrolink.product.repository.ProductRepository;
import com.hanrolink.product.repository.ProductStoryRepository;
import com.hanrolink.product.request.SupplierProductCreateRequest;
import com.hanrolink.product.request.SupplierProductUpdateVisibilityRequest;
import com.hanrolink.product.request.SupplierProductUpdateRequest;
import com.hanrolink.product.request.component.MonthlySupplyCapacityRequest;
import com.hanrolink.product.request.component.ProductStoryCreateRequest;
import com.hanrolink.product.request.component.ProductStoryUpdateRequest;
import com.hanrolink.product.response.SupplierProductCreateResponse;
import com.hanrolink.product.response.SupplierProductListResponse;

@Profile("cloudfront")
@Service
public class SupplierProductManagementService {

  private final BusinessUserAccountRepository businessUserAccountRepository;

  private final ProductRepository productRepository;

  private final MonthlySupplyCapacityRepository monthlySupplyCapacityRepository;

  private final ProductStoryRepository productStoryRepository;

  private final PendingFileUploadRepository pendingFileUploadRepository;

  private final PendingFileDeletionService pendingFileDeletionService;

  private final S3UploadedFileVerifier s3UploadedFileVerifier;

  private final CloudFrontDownloadUrlGenerator cloudFrontDownloadUrlGenerator;

  public SupplierProductManagementService(
    BusinessUserAccountRepository businessUserAccountRepository,
    ProductRepository productRepository,
    MonthlySupplyCapacityRepository monthlySupplyCapacityRepository,
    ProductStoryRepository productStoryRepository,
    PendingFileUploadRepository pendingFileUploadRepository,
    PendingFileDeletionService pendingFileDeletionService,
    S3UploadedFileVerifier s3UploadedFileVerifier,
    CloudFrontDownloadUrlGenerator cloudFrontDownloadUrlGenerator
  ) {
    this.businessUserAccountRepository = businessUserAccountRepository;
    this.productRepository = productRepository;
    this.monthlySupplyCapacityRepository = monthlySupplyCapacityRepository;
    this.productStoryRepository = productStoryRepository;
    this.pendingFileUploadRepository = pendingFileUploadRepository;
    this.pendingFileDeletionService = pendingFileDeletionService;
    this.s3UploadedFileVerifier = s3UploadedFileVerifier;
    this.cloudFrontDownloadUrlGenerator = cloudFrontDownloadUrlGenerator;
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
    // 画像のアップロード情報の検証
    PendingFileUpload mainImageUpload = findUsablePendingFileUpload(
      request.mainImagePendingFileUploadId(),
      identityProviderSubject,
      FileUploadUsage.PRODUCT_MAIN_IMAGE
    );

    Map<UUID, PendingFileUpload> storyImageUploadsByPendingFileUploadId =
      new HashMap<>();

    for (ProductStoryCreateRequest productStoryRequest : request.productStories()) {
      PendingFileUpload storyImageUpload = findUsablePendingFileUpload(
        productStoryRequest.pendingFileUploadId(),
        identityProviderSubject,
        FileUploadUsage.PRODUCT_STORY_IMAGE
      );

      storyImageUploadsByPendingFileUploadId.put(
        productStoryRequest.pendingFileUploadId(),
        storyImageUpload
      );
    }

    // 商品と関連情報の保存
    Long supplierBusinessId = businessUserAccountRepository
      .findBusinessIdByIdentityProviderSubject(identityProviderSubject)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    Product product = new Product(
      supplierBusinessId,
      request.productCategoryId(),
      request.mainIngredientOriginPrefectureId(),
      request.name(),
      mainImageUpload.getStorageKey(),
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
        .map(productStoryRequest -> {
          PendingFileUpload storyImageUpload =
            storyImageUploadsByPendingFileUploadId.get(
              productStoryRequest.pendingFileUploadId()
            );

          return new ProductStory(
            savedProduct.getId(),
            productStoryRequest.productStorySectionTemplateId(),
            productStoryRequest.position(),
            productStoryRequest.body(),
            storyImageUpload.getStorageKey()
          );
        })
        .toList();
    productStoryRepository.saveAll(productStories);

    // 商品へ紐付けたアップロード待ち情報の削除
    pendingFileUploadRepository.delete(mainImageUpload);
    pendingFileUploadRepository.deleteAll(
      storyImageUploadsByPendingFileUploadId.values()
    );

    return new SupplierProductCreateResponse(
      savedProduct.getPublicId()
    );
  }

  /**
   * 自社に紐づく商品一覧を取得する
   * @param identityProviderSubject 認証プロバイダーのユーザー識別子
   * @return 商品一覧
   */
  @Transactional(readOnly = true)
  public List<SupplierProductListResponse> list(
    String identityProviderSubject
  ) {
    return productRepository
      .findManagementListByIdentityProviderSubject(identityProviderSubject)
      .stream()
      .map(product ->
        new SupplierProductListResponse(
          product.publicId(),
          product.name(),
          cloudFrontDownloadUrlGenerator.generate(product.mainImageStorageKey()),
          product.hiddenAt() != null,
          product.updatedAt()
        )
      )
      .toList();
  }

  /**
   * 自社に紐づく商品情報を更新する
   * @param identityProviderSubject 認証プロバイダーのユーザー識別子
   * @param productPublicId 更新対象の商品の公開識別子
   * @param request 商品の更新情報
   */
  @Transactional
  public void update(
    String identityProviderSubject,
    UUID productPublicId,
    SupplierProductUpdateRequest request
  ) {
    // 商品と関連情報の取得
    Product product = productRepository
      .findByPublicIdAndIdentityProviderSubject(productPublicId, identityProviderSubject)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    List<MonthlySupplyCapacity> monthlySupplyCapacities =
      monthlySupplyCapacityRepository.findAllByProductId(product.getId());
    List<ProductStory> productStories =
      productStoryRepository.findAllByProductId(product.getId());

    // リクエストの商品ストーリーIDと登録済みIDの完全一致確認
    Set<Long> registeredProductStoryIds =
      productStories
        .stream()
        .map(productStory ->
          productStory.getId()
        )
        .collect(Collectors.toSet());

    List<Long> requestedProductStoryIdList =
      request.productStories()
        .stream()
        .map(productStoryUpdateRequest ->
          productStoryUpdateRequest.id()
        )
        .toList();

    Set<Long> requestedProductStoryIds =
      new HashSet<>(requestedProductStoryIdList);

    if (requestedProductStoryIds.size() != requestedProductStoryIdList.size()
      || !registeredProductStoryIds.equals(requestedProductStoryIds)
    ) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    // 代表画像が指定された場合の利用可否確認と画像の差し替え
    if (request.mainImagePendingFileUploadId() != null) {
      PendingFileUpload mainImageUpload = findUsablePendingFileUpload(
        request.mainImagePendingFileUploadId(),
        identityProviderSubject,
        FileUploadUsage.PRODUCT_MAIN_IMAGE
      );
      String oldMainImageStorageKey = product.getMainImageStorageKey();

      product.updateMainImageStorageKey(mainImageUpload.getStorageKey());
      pendingFileDeletionService.create(oldMainImageStorageKey);
      pendingFileUploadRepository.delete(mainImageUpload);
    }

    // 後続の差し替えに使用する商品ストーリー画像の利用可否確認
    Map<Long, PendingFileUpload> storyImageUploadsByProductStoryId = new HashMap<>();

    for (ProductStoryUpdateRequest productStoryRequest : request.productStories()) {
      if (productStoryRequest.pendingFileUploadId() == null) {
        continue;
      }

      PendingFileUpload storyImageUpload = findUsablePendingFileUpload(
        productStoryRequest.pendingFileUploadId(),
        identityProviderSubject,
        FileUploadUsage.PRODUCT_STORY_IMAGE
      );

      storyImageUploadsByProductStoryId.put(
        productStoryRequest.id(),
        storyImageUpload
      );
    }

    // 商品の基本情報の更新
    product.update(
      request.productCategoryId(),
      request.mainIngredientOriginPrefectureId(),
      request.name(),
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

    // 月間供給可能数の対象月ごとの差分反映
    Map<YearMonth, MonthlySupplyCapacity> existingMonthlySupplyCapacitiesByTargetMonth =
      monthlySupplyCapacities
        .stream()
        .collect(
          Collectors.toMap(
            monthlySupplyCapacity -> monthlySupplyCapacity.getTargetMonth(),
            monthlySupplyCapacity -> monthlySupplyCapacity
          )
        );

    Set<YearMonth> requestedTargetMonths = new HashSet<>();
    List<MonthlySupplyCapacity> newMonthlySupplyCapacities = new ArrayList<>();

    for (MonthlySupplyCapacityRequest monthlySupplyCapacityRequest
      : request.monthlySupplyCapacities()
    ) {
      YearMonth targetMonth = monthlySupplyCapacityRequest.targetMonth();
      requestedTargetMonths.add(targetMonth);

      MonthlySupplyCapacity existingMonthlySupplyCapacity =
        existingMonthlySupplyCapacitiesByTargetMonth.get(targetMonth);

      if (existingMonthlySupplyCapacity == null) {
        newMonthlySupplyCapacities.add(
          new MonthlySupplyCapacity(
            product.getId(),
            targetMonth,
            monthlySupplyCapacityRequest.availableQuantity()
          )
        );
        continue;
      }

      existingMonthlySupplyCapacity.updateAvailableQuantity(
        monthlySupplyCapacityRequest.availableQuantity()
      );
    }

    List<MonthlySupplyCapacity> unusedMonthlySupplyCapacities =
      monthlySupplyCapacities
        .stream()
        .filter(monthlySupplyCapacity ->
          !requestedTargetMonths.contains(
            monthlySupplyCapacity.getTargetMonth()
          )
        )
        .toList();

    monthlySupplyCapacityRepository.deleteAll(unusedMonthlySupplyCapacities);
    monthlySupplyCapacityRepository.saveAll(newMonthlySupplyCapacities);

    // 商品ストーリーの内容更新と指定された画像の差し替え
    Map<Long, ProductStory> existingProductStoriesById =
      productStories
        .stream()
        .collect(
          Collectors.toMap(
            productStory -> productStory.getId(),
            productStory -> productStory
          )
        );

    for (ProductStoryUpdateRequest productStoryRequest : request.productStories()) {
      ProductStory existingProductStory = existingProductStoriesById.get(
        productStoryRequest.id()
      );

      existingProductStory.update(
        productStoryRequest.productStorySectionTemplateId(),
        productStoryRequest.position(),
        productStoryRequest.body()
      );

      PendingFileUpload storyImageUpload =
        storyImageUploadsByProductStoryId.get(
          productStoryRequest.id()
        );

      if (storyImageUpload == null) {
        continue;
      }

      String oldImageStorageKey = existingProductStory.getImageStorageKey();

      existingProductStory.updateImageStorageKey(
        storyImageUpload.getStorageKey()
      );
      pendingFileDeletionService.create(oldImageStorageKey);
      pendingFileUploadRepository.delete(storyImageUpload);
    }
  }

  /**
   * 自社に紐づく商品の表示状態を更新する
   * @param identityProviderSubject 認証プロバイダーのユーザー識別子
   * @param productPublicId 更新対象の商品の公開識別子
   * @param request 表示状態の更新情報
   */
  @Transactional
  public void updateVisibility(
    String identityProviderSubject,
    UUID productPublicId,
    SupplierProductUpdateVisibilityRequest request
  ) {
    Product product = productRepository
      .findByPublicIdAndIdentityProviderSubject(productPublicId, identityProviderSubject)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    product.updateVisibility(request.hidden());
  }

  /**
   * 自社に紐づく商品を削除する
   * @param identityProviderSubject 認証プロバイダーのユーザー識別子
   * @param productPublicId 削除対象の商品の公開識別子
   */
  @Transactional
  public void delete(
    String identityProviderSubject,
    UUID productPublicId
  ) {
    Product product = productRepository
      .findByPublicIdAndIdentityProviderSubject(productPublicId, identityProviderSubject)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    // 商品を削除する前に、関連画像を削除待ちとして登録
    List<String> imageStorageKeys = productStoryRepository
      .findImageStorageKeysByProductId(product.getId());

    for (String imageStorageKey : imageStorageKeys) {
      pendingFileDeletionService.create(imageStorageKey);
    }
    pendingFileDeletionService.create(product.getMainImageStorageKey());

    productRepository.delete(product);
  }

  private PendingFileUpload findUsablePendingFileUpload(
    UUID pendingFileUploadId,
    String identityProviderSubject,
    FileUploadUsage expectedUsage
  ) {
    PendingFileUpload pendingFileUpload = pendingFileUploadRepository
      .findByPublicIdAndIdentityProviderSubject(
        pendingFileUploadId,
        identityProviderSubject
      )
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    if (pendingFileUpload.getUsage() != expectedUsage) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    Instant expiresAt = pendingFileUpload
      .getCreatedAt()
      .plus(PendingFileUploadPolicy.VALID_DURATION);

    if (!expiresAt.isAfter(Instant.now())) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    boolean isValidWebp = s3UploadedFileVerifier.isValidWebp(
      pendingFileUpload.getStorageKey(),
      pendingFileUpload.getFileSizeBytes()
    );

    if (!isValidWebp) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "アップロードされたファイルを確認できません"
      );
    }

    return pendingFileUpload;
  }
}
