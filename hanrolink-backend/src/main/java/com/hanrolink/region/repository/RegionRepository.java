package com.hanrolink.region.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hanrolink.region.entity.Region;
import com.hanrolink.region.repository.projection.RegionOptionProjection;

@Repository
public interface RegionRepository extends JpaRepository<Region, Short> {

  @Query("""
    SELECT new com.hanrolink.region.repository.projection.RegionOptionProjection(
      region.id,
      region.name
    )
    FROM Region region
    ORDER BY region.sortOrder ASC
    """)
  List<RegionOptionProjection> findAllOptions();
}
