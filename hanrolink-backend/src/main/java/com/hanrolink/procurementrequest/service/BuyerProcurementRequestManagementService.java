package com.hanrolink.procurementrequest.service;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.hanrolink.account.repository.BusinessUserAccountRepository;
import com.hanrolink.procurementrequest.entity.MonthlyProcurementQuantity;
import com.hanrolink.procurementrequest.entity.ProcurementRequest;
import com.hanrolink.procurementrequest.entity.ProcurementRequestStorageType;
import com.hanrolink.procurementrequest.repository.MonthlyProcurementQuantityRepository;
import com.hanrolink.procurementrequest.repository.ProcurementRequestRepository;
import com.hanrolink.procurementrequest.repository.ProcurementRequestStorageTypeRepository;
import com.hanrolink.procurementrequest.request.BuyerProcurementRequestCreateRequest;
import com.hanrolink.procurementrequest.request.BuyerProcurementRequestUpdateRequest;
import com.hanrolink.procurementrequest.request.component.MonthlyProcurementQuantityRequest;
import com.hanrolink.procurementrequest.response.BuyerProcurementRequestCreateResponse;
import com.hanrolink.product.enums.StorageType;

@Service
public class BuyerProcurementRequestManagementService {

  private final ProcurementRequestRepository procurementRequestRepository;

  private final ProcurementRequestStorageTypeRepository procurementRequestStorageTypeRepository;

  private final MonthlyProcurementQuantityRepository monthlyProcurementQuantityRepository;

  private final BusinessUserAccountRepository businessUserAccountRepository;

  public BuyerProcurementRequestManagementService(
    ProcurementRequestRepository procurementRequestRepository,
    ProcurementRequestStorageTypeRepository procurementRequestStorageTypeRepository,
    MonthlyProcurementQuantityRepository monthlyProcurementQuantityRepository,
    BusinessUserAccountRepository businessUserAccountRepository
  ) {
    this.procurementRequestRepository = procurementRequestRepository;
    this.procurementRequestStorageTypeRepository = procurementRequestStorageTypeRepository;
    this.monthlyProcurementQuantityRepository = monthlyProcurementQuantityRepository;
    this.businessUserAccountRepository = businessUserAccountRepository;
  }

  /**
   * 募集情報を新規作成する
   * @param identityProviderSubject 認証プロバイダーのユーザー識別子
   * @param request 募集の入力情報
   * @return 募集の作成結果
   */
  @Transactional
  public BuyerProcurementRequestCreateResponse create(
    String identityProviderSubject,
    BuyerProcurementRequestCreateRequest request
  ) {
    Long buyerBusinessId = businessUserAccountRepository
      .findBusinessIdByIdentityProviderSubject(identityProviderSubject)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    ProcurementRequest procurementRequest = new ProcurementRequest(
      buyerBusinessId,
      request.productCategoryId(),
      request.title(),
      request.description(),
      request.requiredTradeTerms(),
      request.desiredUnitPrice(),
      request.deliveryShelfLifeDays()
    );
    ProcurementRequest savedProcurementRequest =
      procurementRequestRepository.save(procurementRequest);

    List<ProcurementRequestStorageType> procurementRequestStorageTypes =
      request.storageTypes()
        .stream()
        .map(storageType ->
          new ProcurementRequestStorageType(
            savedProcurementRequest.getId(),
            storageType
          )
        )
        .toList();
    procurementRequestStorageTypeRepository.saveAll(procurementRequestStorageTypes);

    List<MonthlyProcurementQuantity> monthlyProcurementQuantities =
      request.monthlyProcurementQuantities()
        .stream()
        .map(monthlyProcurementQuantity ->
          new MonthlyProcurementQuantity(
            savedProcurementRequest.getId(),
            monthlyProcurementQuantity.targetMonth(),
            monthlyProcurementQuantity.desiredQuantity()
          )
        )
        .toList();
    monthlyProcurementQuantityRepository.saveAll(monthlyProcurementQuantities);

    return new BuyerProcurementRequestCreateResponse(
      savedProcurementRequest.getPublicId()
    );
  }

  /**
   * 自社に紐づく募集情報を更新する
   * @param identityProviderSubject 認証プロバイダーのユーザー識別子
   * @param procurementRequestPublicId 更新対象の募集の公開識別子
   * @param request 募集の更新情報
   */
  @Transactional
  public void update(
    String identityProviderSubject,
    UUID procurementRequestPublicId,
    BuyerProcurementRequestUpdateRequest request
  ) {
    // 募集や既存関連情報の取得
    ProcurementRequest procurementRequest = procurementRequestRepository
      .findByPublicIdAndIdentityProviderSubject(
        procurementRequestPublicId,
        identityProviderSubject
      )
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    List<ProcurementRequestStorageType> procurementRequestStorageTypes =
      procurementRequestStorageTypeRepository
        .findAllByProcurementRequestId(procurementRequest.getId());

    List<MonthlyProcurementQuantity> monthlyProcurementQuantities =
      monthlyProcurementQuantityRepository
        .findAllByProcurementRequestId(procurementRequest.getId());

    // 募集情報の更新
    procurementRequest.update(
      request.productCategoryId(),
      request.title(),
      request.description(),
      request.requiredTradeTerms(),
      request.desiredUnitPrice(),
      request.deliveryShelfLifeDays()
    );

    // 保存方法の差分反映
    Set<StorageType> existingStorageTypes =
      procurementRequestStorageTypes
        .stream()
        .map(procurementRequestStorageType ->
          procurementRequestStorageType.getStorageType()
        )
        .collect(Collectors.toSet());

    List<ProcurementRequestStorageType> unusedStorageTypes =
      procurementRequestStorageTypes
        .stream()
        .filter(procurementRequestStorageType ->
          !request.storageTypes().contains(
            procurementRequestStorageType.getStorageType()
          )
        )
        .toList();

    List<ProcurementRequestStorageType> newStorageTypes =
      request.storageTypes()
        .stream()
        .filter(storageType ->
          !existingStorageTypes.contains(storageType)
        )
        .map(storageType ->
          new ProcurementRequestStorageType(
            procurementRequest.getId(),
            storageType
          )
        )
        .toList();

    procurementRequestStorageTypeRepository.deleteAll(unusedStorageTypes);
    procurementRequestStorageTypeRepository.saveAll(newStorageTypes);

    // 月別調達数量の対象月ごとの差分反映
    Map<YearMonth, MonthlyProcurementQuantity> monthlyProcurementQuantitiesByTargetMonth =
      monthlyProcurementQuantities
        .stream()
        .collect(
          Collectors.toMap(
            monthlyProcurementQuantity -> monthlyProcurementQuantity.getTargetMonth(),
            monthlyProcurementQuantity -> monthlyProcurementQuantity
          )
        );

    Set<YearMonth> requestedTargetMonths = new HashSet<>();
    List<MonthlyProcurementQuantity> newMonthlyProcurementQuantities = new ArrayList<>();

    for (MonthlyProcurementQuantityRequest monthlyProcurementQuantityRequest
      : request.monthlyProcurementQuantities()
    ) {
      YearMonth targetMonth = monthlyProcurementQuantityRequest.targetMonth();
      requestedTargetMonths.add(targetMonth);

      MonthlyProcurementQuantity existingMonthlyProcurementQuantity =
        monthlyProcurementQuantitiesByTargetMonth.get(targetMonth);
      if (existingMonthlyProcurementQuantity == null) {
        newMonthlyProcurementQuantities.add(
          new MonthlyProcurementQuantity(
            procurementRequest.getId(),
            targetMonth,
            monthlyProcurementQuantityRequest.desiredQuantity()
          )
        );
        continue;
      }

      existingMonthlyProcurementQuantity.updateDesiredQuantity(
        monthlyProcurementQuantityRequest.desiredQuantity()
      );
    }

    List<MonthlyProcurementQuantity> unusedMonthlyProcurementQuantities =
      monthlyProcurementQuantities
        .stream()
        .filter(
          monthlyProcurementQuantity ->
            !requestedTargetMonths.contains(
              monthlyProcurementQuantity.getTargetMonth()
            )
        )
        .toList();
    monthlyProcurementQuantityRepository.deleteAll(unusedMonthlyProcurementQuantities);
    monthlyProcurementQuantityRepository.saveAll(newMonthlyProcurementQuantities);
  }
}
