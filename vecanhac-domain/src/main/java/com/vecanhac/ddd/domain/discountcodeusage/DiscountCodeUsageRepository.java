package com.vecanhac.ddd.domain.discountcodeusage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DiscountCodeUsageRepository extends JpaRepository<DiscountCodeUsageEntity, Long> {

    // Sử dụng đúng tên field trong entity (quan hệ)
    Optional<DiscountCodeUsageEntity> findByUser_IdAndDiscountCode_Id(Long userId, Long discountCodeId);

    // Đếm số lần dùng thành công cho toàn hệ thống
    long countByDiscountCode_IdAndIsSuccessfulTrue(Long discountCodeId);

    // Đếm số lần dùng thành công theo user
    long countByUser_IdAndDiscountCode_IdAndIsSuccessfulTrue(Long userId, Long discountCodeId);

    // Nếu bạn vẫn muốn dùng custom query
    @Query("SELECT COUNT(u) FROM DiscountCodeUsageEntity u WHERE u.discountCode.id = :discountCodeId AND u.isSuccessful = true")
    long countTotalSuccessfulUsage(@Param("discountCodeId") Long discountCodeId);

    @Query("SELECT COUNT(u) FROM DiscountCodeUsageEntity u WHERE u.discountCode.id = :discountCodeId AND u.user.id = :userId AND u.isSuccessful = true")
    long countSuccessfulUsageByUser(@Param("discountCodeId") Long discountCodeId,
                                    @Param("userId") Long userId);
}
