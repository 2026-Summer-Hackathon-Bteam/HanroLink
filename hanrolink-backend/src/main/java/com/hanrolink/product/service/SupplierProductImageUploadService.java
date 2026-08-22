package com.hanrolink.product.service;

import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.hanrolink.account.repository.BusinessUserAccountRepository;
import com.hanrolink.account.repository.projection.FileUploadContextProjection;
import com.hanrolink.file.entity.PendingFileUpload;
import com.hanrolink.file.enums.FileMimeType;
import com.hanrolink.file.enums.FileUploadUsage;
import com.hanrolink.file.repository.PendingFileUploadRepository;
import com.hanrolink.infrastructure.s3.S3UploadUrlGenerator;
import com.hanrolink.product.enums.ProductImageUsage;
import com.hanrolink.product.request.SupplierProductImageUploadCreateRequest;
import com.hanrolink.product.response.SupplierProductImageUploadCreateResponse;

@Profile("s3")
@Service
public class SupplierProductImageUploadService {

  private static final FileMimeType PRODUCT_IMAGE_MIME_TYPE = FileMimeType.IMAGE_WEBP;

  private final BusinessUserAccountRepository businessUserAccountRepository;

  private final PendingFileUploadRepository pendingFileUploadRepository;

  private final S3UploadUrlGenerator s3UploadUrlGenerator;

  public SupplierProductImageUploadService(
    BusinessUserAccountRepository businessUserAccountRepository,
    PendingFileUploadRepository pendingFileUploadRepository,
    S3UploadUrlGenerator s3UploadUrlGenerator
  ) {
    this.businessUserAccountRepository = businessUserAccountRepository;
    this.pendingFileUploadRepository = pendingFileUploadRepository;
    this.s3UploadUrlGenerator = s3UploadUrlGenerator;
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
    FileUploadContextProjection fileUploadContext = businessUserAccountRepository
      .findFileUploadContextByIdentityProviderSubject(identityProviderSubject)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    String imageStorageKey = createStorageKey(
      request.usage(),
      fileUploadContext.businessPublicId()
    );

    String uploadUrl = s3UploadUrlGenerator.generate(
      imageStorageKey,
      PRODUCT_IMAGE_MIME_TYPE.getValue()
    );

    PendingFileUpload pendingFileUpload =
      new PendingFileUpload(
        fileUploadContext.businessUserAccountId(),
        null,
        imageStorageKey,
        fileUploadUsageOf(request.usage()),
        null,
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
    ProductImageUsage usage,
    UUID businessPublicId
  ) {
    String imageDirectory =
      switch (usage) {
        case MAIN_IMAGE -> "main-images";
        case STORY_IMAGE -> "story-images";
      };
    return "products/"
      + businessPublicId
      + "/"
      + imageDirectory
      + "/"
      + UUID.randomUUID()
      + "."
      + PRODUCT_IMAGE_MIME_TYPE.getExtension();
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
