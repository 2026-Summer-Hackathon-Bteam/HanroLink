package com.hanrolink.region.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hanrolink.region.entity.Region;
import com.hanrolink.region.response.component.RegionOptionResponse;

public interface RegionRepository extends JpaRepository<Region, Short> {

  @Query("""
    SELECT new com.hanrolink.region.response.component.RegionOptionResponse(
      region.id,
      region.name
    )
    FROM Region region
    ORDER BY region.sortOrder ASC
    """)
  List<RegionOptionResponse> findAllOptions();
}
