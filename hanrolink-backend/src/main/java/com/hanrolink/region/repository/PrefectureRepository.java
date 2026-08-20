package com.hanrolink.region.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hanrolink.region.entity.Prefecture;
import com.hanrolink.region.repository.projection.PrefectureOptionProjection;

@Repository
public interface PrefectureRepository extends JpaRepository<Prefecture, Short> {

  @Query("""
    SELECT new com.hanrolink.region.repository.projection.PrefectureOptionProjection(
      prefecture.id,
      prefecture.name
    )
    FROM Prefecture prefecture
    ORDER BY prefecture.id ASC
    """)
  List<PrefectureOptionProjection> findAllOptions();
}
