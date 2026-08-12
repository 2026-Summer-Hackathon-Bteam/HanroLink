package com.hanrolink.product.service;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.hanrolink.account.repository.BusinessUserAccountRepository;
import com.hanrolink.file.entity.PendingFileUpload;
import com.hanrolink.file.enums.FileUploadUsage;
import com.hanrolink.file.repository.PendingFileUploadRepository;
import com.hanrolink.product.enums.ProductImageUsage;
import com.hanrolink.product.request.SupplierProductImageUploadCreateRequest;
import com.hanrolink.product.response.SupplierProductImageUploadCreateResponse;

@Service
public class SupplierProductImageUploadService {

  private static final String PRODUCT_IMAGE_MIME_TYPE = "image/webp";

  private final BusinessUserAccountRepository businessUserAccountRepository;

  private final PendingFileUploadRepository pendingFileUploadRepository;

  public SupplierProductImageUploadService(
    BusinessUserAccountRepository businessUserAccountRepository,
    PendingFileUploadRepository pendingFileUploadRepository
  ) {
    this.businessUserAccountRepository = businessUserAccountRepository;
    this.pendingFileUploadRepository = pendingFileUploadRepository;
  }

  /**
   * 商品画像のアップロード情報を作成する
   * @param identityProviderSubject 認証プロバイダーのユーザー識別子
   * @param request 商品画像のアップロード情報
   * @return 作成した商品画像のアップロード情報
   */
  @Transactional
  public SupplierProductImageUploadCreateResponse create(
    String identityProviderSubject,
    SupplierProductImageUploadCreateRequest request
  ) {
    Long supplierAccountId =
      businessUserAccountRepository
        .findIdByIdentityProviderSubject(identityProviderSubject)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    String imageStorageKey = createStorageKey(request.usage());

    // TODO: S3連携時に署名付きURLを生成し、ダミー値を置き換える
    String uploadUrl = "dummy/" + UUID.randomUUID();

    PendingFileUpload pendingFileUpload =
      new PendingFileUpload(
        supplierAccountId,
        imageStorageKey,
        fileUploadUsageOf(request.usage()),
        PRODUCT_IMAGE_MIME_TYPE,
        request.fileSizeBytes()
      );

    pendingFileUploadRepository.save(pendingFileUpload);

    return new SupplierProductImageUploadCreateResponse(
      uploadUrl,
      pendingFileUpload.getPublicId()
    );
  }

  private String createStorageKey(
    ProductImageUsage usage
  ) {
    return switch (usage) {
      case MAIN_IMAGE ->
        "products/main-images/" + UUID.randomUUID() + ".webp";
      case STORY_IMAGE ->
        "products/story-images/" + UUID.randomUUID() + ".webp";
    };
  }

  private FileUploadUsage fileUploadUsageOf(
    ProductImageUsage usage
  ) {
    return switch (usage) {
      case MAIN_IMAGE ->
        FileUploadUsage.PRODUCT_MAIN_IMAGE;
      case STORY_IMAGE ->
        FileUploadUsage.PRODUCT_STORY_IMAGE;
    };
  }
}
