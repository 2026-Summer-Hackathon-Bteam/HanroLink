package com.hanrolink.procurementrequest.service;

import java.util.List;

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
import com.hanrolink.procurementrequest.response.BuyerProcurementRequestCreateResponse;

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
}
