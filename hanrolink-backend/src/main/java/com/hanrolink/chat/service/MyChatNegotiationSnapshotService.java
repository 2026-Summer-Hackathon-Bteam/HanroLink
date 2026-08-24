package com.hanrolink.chat.service;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.hanrolink.chat.enums.NegotiationTargetType;
import com.hanrolink.chat.enums.ProcurementRequestSnapshotField;
import com.hanrolink.chat.enums.ProductSnapshotField;
import com.hanrolink.chat.repository.ChannelRepository;
import com.hanrolink.chat.repository.projection.MyChatNegotiationSnapshotProjection;
import com.hanrolink.chat.response.MyChatNegotiationSnapshotResponse;
import com.hanrolink.chat.response.component.MonthlyProcurementQuantitySnapshotResponse;
import com.hanrolink.chat.response.component.MonthlySupplyCapacitySnapshotResponse;
import com.hanrolink.chat.response.component.ProcurementRequestSnapshotResponse;
import com.hanrolink.chat.response.component.ProductSnapshotResponse;
import com.hanrolink.chat.response.component.ProductStorySnapshotResponse;
import com.hanrolink.negotiationrequest.snapshot.ProcurementRequestSnapshot;
import com.hanrolink.negotiationrequest.snapshot.ProductSnapshot;

@Service
public class MyChatNegotiationSnapshotService {

  private final ChannelRepository channelRepository;

  public MyChatNegotiationSnapshotService(
    ChannelRepository channelRepository
  ) {
    this.channelRepository = channelRepository;
  }

  /**
   * 指定されたチャンネルの商談希望時点と承諾時点のスナップショットおよび変更項目を取得する
   * @param identityProviderSubject 認証プロバイダーのユーザー識別子
   * @param channelPublicId 取得対象のチャンネル公開識別子
   * @return 商談希望時点・承諾時点のスナップショットと変更項目
   */
  @Transactional(readOnly = true)
  public MyChatNegotiationSnapshotResponse get(
    String identityProviderSubject,
    UUID channelPublicId
  ) {
    MyChatNegotiationSnapshotProjection negotiationSnapshot =
      channelRepository
        .findNegotiationSnapshotByPublicIdAndIdentityProviderSubject(
          channelPublicId,
          identityProviderSubject
        )
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    Set<ProductSnapshotField> productChangedFields = compareProductSnapshots(
      negotiationSnapshot.requestedProductSnapshot(),
      negotiationSnapshot.acceptedProductSnapshot()
    );
    ProductSnapshotResponse requestedProductSnapshot =
      toProductSnapshotResponse(
        negotiationSnapshot.requestedProductSnapshot()
      );

    ProductSnapshotResponse acceptedProductSnapshot =
      toProductSnapshotResponse(
        negotiationSnapshot.acceptedProductSnapshot()
      );

    Set<ProcurementRequestSnapshotField> procurementRequestChangedFields =
      Set.of();
    ProcurementRequestSnapshotResponse requestedProcurementRequestSnapshot = null;
    ProcurementRequestSnapshotResponse acceptedProcurementRequestSnapshot = null;
    if (negotiationSnapshot.negotiationTargetType() == NegotiationTargetType.PROCUREMENT_REQUEST) {
      procurementRequestChangedFields =
        compareProcurementRequestSnapshots(
          negotiationSnapshot.requestedProcurementRequestSnapshot(),
          negotiationSnapshot.acceptedProcurementRequestSnapshot()
        );

      requestedProcurementRequestSnapshot =
        toProcurementRequestSnapshotResponse(
          negotiationSnapshot.requestedProcurementRequestSnapshot()
        );

      acceptedProcurementRequestSnapshot =
        toProcurementRequestSnapshotResponse(
          negotiationSnapshot.acceptedProcurementRequestSnapshot()
        );
    }

    return new MyChatNegotiationSnapshotResponse(
      negotiationSnapshot.negotiationTargetType(),
      productChangedFields,
      requestedProductSnapshot,
      acceptedProductSnapshot,
      procurementRequestChangedFields,
      requestedProcurementRequestSnapshot,
      acceptedProcurementRequestSnapshot
    );
  }

  private Set<ProductSnapshotField> compareProductSnapshots(
    ProductSnapshot requestedSnapshot,
    ProductSnapshot acceptedSnapshot
  ) {
    if (Objects.equals(
        requestedSnapshot.sourceUpdatedAt(),
        acceptedSnapshot.sourceUpdatedAt()
      )
    ) {
      return Set.of();
    }

    Set<ProductSnapshotField> changedFields =
      EnumSet.noneOf(ProductSnapshotField.class);

    if (!Objects.equals(
        requestedSnapshot.productCategory().id(),
        acceptedSnapshot.productCategory().id()
      )
    ) {
      changedFields.add(ProductSnapshotField.PRODUCT_CATEGORY);
    }

    if (!Objects.equals(
        requestedSnapshot.mainIngredientOriginPrefecture().id(),
        acceptedSnapshot.mainIngredientOriginPrefecture().id()
      )
    ) {
      changedFields.add(ProductSnapshotField.MAIN_INGREDIENT_ORIGIN_PREFECTURE);
    }

    if (!Objects.equals(
        requestedSnapshot.name(),
        acceptedSnapshot.name()
      )
    ) {
      changedFields.add(ProductSnapshotField.NAME);
    }

    if (!Objects.equals(
        requestedSnapshot.contentQuantity(),
        acceptedSnapshot.contentQuantity()
      )
    ) {
      changedFields.add(ProductSnapshotField.CONTENT_QUANTITY);
    }

    if (!Objects.equals(
        requestedSnapshot.expirationType().value(),
        acceptedSnapshot.expirationType().value()
      )
    ) {
      changedFields.add(ProductSnapshotField.EXPIRATION_TYPE);
    }

    if (!Objects.equals(
        requestedSnapshot.shelfLifeDays(),
        acceptedSnapshot.shelfLifeDays()
      )
    ) {
      changedFields.add(ProductSnapshotField.SHELF_LIFE_DAYS);
    }

    if (!Objects.equals(
        requestedSnapshot.storageType().value(),
        acceptedSnapshot.storageType().value()
      )
    ) {
      changedFields.add(ProductSnapshotField.STORAGE_TYPE);
    }

    if (!Objects.equals(
        requestedSnapshot.desiredRetailPrice(),
        acceptedSnapshot.desiredRetailPrice()
      )
    ) {
      changedFields.add(ProductSnapshotField.DESIRED_RETAIL_PRICE);
    }

    if (!Objects.equals(
        requestedSnapshot.allergyInformation(),
        acceptedSnapshot.allergyInformation()
      )
    ) {
      changedFields.add(ProductSnapshotField.ALLERGY_INFORMATION);
    }

    if (!Objects.equals(
        requestedSnapshot.certificationInformation(),
        acceptedSnapshot.certificationInformation()
      )
    ) {
      changedFields.add(ProductSnapshotField.CERTIFICATION_INFORMATION);
    }

    if (!Objects.equals(
        requestedSnapshot.caseSize(),
        acceptedSnapshot.caseSize()
      )
    ) {
      changedFields.add(ProductSnapshotField.CASE_SIZE);
    }

    if (!Objects.equals(
        requestedSnapshot.unitsPerCase(),
        acceptedSnapshot.unitsPerCase()
      )
    ) {
      changedFields.add(ProductSnapshotField.UNITS_PER_CASE);
    }

    if (!Objects.equals(
        requestedSnapshot.minimumOrderQuantity(),
        acceptedSnapshot.minimumOrderQuantity()
      )
    ) {
      changedFields.add(ProductSnapshotField.MINIMUM_ORDER_QUANTITY);
    }

    if (!Objects.equals(
        requestedSnapshot.shippingLeadTimeDays(),
        acceptedSnapshot.shippingLeadTimeDays()
      )
    ) {
      changedFields.add(ProductSnapshotField.SHIPPING_LEAD_TIME_DAYS);
    }

    if (!Objects.equals(
        requestedSnapshot.salesAreaRestriction(),
        acceptedSnapshot.salesAreaRestriction()
      )
    ) {
      changedFields.add(ProductSnapshotField.SALES_AREA_RESTRICTION);
    }

    if (!Objects.equals(
        new HashSet<>(requestedSnapshot.monthlySupplyCapacities()),
        new HashSet<>(acceptedSnapshot.monthlySupplyCapacities())
      )
    ) {
      changedFields.add(ProductSnapshotField.MONTHLY_SUPPLY_CAPACITIES);
    }

    if (!Objects.equals(
        requestedSnapshot.productStories(),
        acceptedSnapshot.productStories()
      )
    ) {
      changedFields.add(ProductSnapshotField.PRODUCT_STORIES);
    }

    return Set.copyOf(changedFields);
  }

  private Set<ProcurementRequestSnapshotField> compareProcurementRequestSnapshots(
    ProcurementRequestSnapshot requestedSnapshot,
    ProcurementRequestSnapshot acceptedSnapshot
  ) {
    if (Objects.equals(
        requestedSnapshot.sourceUpdatedAt(),
        acceptedSnapshot.sourceUpdatedAt()
      )
    ) {
      return Set.of();
    }

    Set<ProcurementRequestSnapshotField> changedFields =
      EnumSet.noneOf(ProcurementRequestSnapshotField.class);

    if (!Objects.equals(
        requestedSnapshot.productCategory().id(),
        acceptedSnapshot.productCategory().id()
      )
    ) {
      changedFields.add(ProcurementRequestSnapshotField.PRODUCT_CATEGORY);
    }

    if (!Objects.equals(
        requestedSnapshot.title(),
        acceptedSnapshot.title()
      )
    ) {
      changedFields.add(ProcurementRequestSnapshotField.TITLE);
    }

    if (!Objects.equals(
        requestedSnapshot.description(),
        acceptedSnapshot.description()
      )
    ) {
      changedFields.add(ProcurementRequestSnapshotField.DESCRIPTION);
    }

    if (!Objects.equals(
        requestedSnapshot.requiredTradeTerms(),
        acceptedSnapshot.requiredTradeTerms()
      )
    ) {
      changedFields.add(ProcurementRequestSnapshotField.REQUIRED_TRADE_TERMS);
    }

    if (!Objects.equals(
        requestedSnapshot.desiredUnitPrice(),
        acceptedSnapshot.desiredUnitPrice()
      )
    ) {
      changedFields.add(ProcurementRequestSnapshotField.DESIRED_UNIT_PRICE);
    }

    if (!Objects.equals(
        requestedSnapshot.deliveryShelfLifeDays(),
        acceptedSnapshot.deliveryShelfLifeDays()
      )
    ) {
      changedFields.add(ProcurementRequestSnapshotField.DELIVERY_SHELF_LIFE_DAYS);
    }

    if (!Objects.equals(
        new HashSet<>(requestedSnapshot.storageTypes()),
        new HashSet<>(acceptedSnapshot.storageTypes())
      )
    ) {
      changedFields.add(ProcurementRequestSnapshotField.STORAGE_TYPES);
    }

    if (!Objects.equals(
        new HashSet<>(requestedSnapshot.monthlyProcurementQuantities()),
        new HashSet<>(acceptedSnapshot.monthlyProcurementQuantities())
      )
    ) {
      changedFields.add(ProcurementRequestSnapshotField.MONTHLY_PROCUREMENT_QUANTITIES);
    }

    return Set.copyOf(changedFields);
  }

  private ProductSnapshotResponse toProductSnapshotResponse(
    ProductSnapshot snapshot
  ) {
    List<MonthlySupplyCapacitySnapshotResponse> monthlySupplyCapacities =
      snapshot.monthlySupplyCapacities()
        .stream()
        .map(monthlySupplyCapacity ->
          new MonthlySupplyCapacitySnapshotResponse(
            monthlySupplyCapacity.targetMonth(),
            monthlySupplyCapacity.availableQuantity()
          )
        )
        .toList();
    List<ProductStorySnapshotResponse> productStories =
      snapshot.productStories()
        .stream()
        .map(productStory ->
          new ProductStorySnapshotResponse(
            productStory.sectionTitle(),
            productStory.body()
          )
        )
        .toList();

    return new ProductSnapshotResponse(
      snapshot.productCategory().name(),
      snapshot.mainIngredientOriginPrefecture().name(),
      snapshot.name(),
      snapshot.contentQuantity(),
      snapshot.expirationType().displayName(),
      snapshot.shelfLifeDays(),
      snapshot.storageType().displayName(),
      snapshot.desiredRetailPrice(),
      snapshot.allergyInformation(),
      snapshot.certificationInformation(),
      snapshot.caseSize(),
      snapshot.unitsPerCase(),
      snapshot.minimumOrderQuantity(),
      snapshot.shippingLeadTimeDays(),
      snapshot.salesAreaRestriction(),
      monthlySupplyCapacities,
      productStories
    );
  }

  private ProcurementRequestSnapshotResponse toProcurementRequestSnapshotResponse(
    ProcurementRequestSnapshot snapshot
  ) {
    List<String> storageTypeNames =
      snapshot.storageTypes()
        .stream()
        .map(storageType ->
          storageType.displayName()
        )
        .toList();

    List<MonthlyProcurementQuantitySnapshotResponse>
      monthlyProcurementQuantities =
        snapshot.monthlyProcurementQuantities()
          .stream()
          .map(monthlyProcurementQuantity ->
            new MonthlyProcurementQuantitySnapshotResponse(
              monthlyProcurementQuantity.targetMonth(),
              monthlyProcurementQuantity.desiredQuantity()
            )
          )
          .toList();

    return new ProcurementRequestSnapshotResponse(
      snapshot.productCategory().name(),
      snapshot.title(),
      snapshot.description(),
      snapshot.requiredTradeTerms(),
      snapshot.desiredUnitPrice(),
      snapshot.deliveryShelfLifeDays(),
      storageTypeNames,
      monthlyProcurementQuantities
    );
  }
}
