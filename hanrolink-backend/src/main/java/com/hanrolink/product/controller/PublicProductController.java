package com.hanrolink.product.controller;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanrolink.product.api.ProductApi;
import com.hanrolink.product.response.PublicProductListResponse;
import com.hanrolink.product.service.PublicProductService;

@Profile("s3")
@RestController
public class PublicProductController {

  private final PublicProductService publicProductService;

  public PublicProductController(
    PublicProductService publicProductService
  ) {
    this.publicProductService = publicProductService;
  }

  /**
   * ゲスト向け画面に掲載する商品一覧を返す
   * @return ゲスト向け画面に掲載する商品一覧
   */
  @GetMapping(ProductApi.V1.BASE_PUBLIC)
  public ResponseEntity<List<PublicProductListResponse>> list() {
    return ResponseEntity.ok(
      publicProductService.list()
    );
  }
}
