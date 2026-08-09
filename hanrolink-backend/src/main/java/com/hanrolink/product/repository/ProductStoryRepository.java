package com.hanrolink.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hanrolink.product.entity.ProductStory;

@Repository
public interface ProductStoryRepository extends JpaRepository<ProductStory, Long> {}
