package com.vecanhac.ddd.domain.discountcode;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscountCodeRepository extends JpaRepository<DiscountCodeEntity, Long> {
    Optional<DiscountCodeEntity> findByCodeAndIsActiveTrue(String code);

}