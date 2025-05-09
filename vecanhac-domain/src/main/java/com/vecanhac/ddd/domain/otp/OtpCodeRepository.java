package com.vecanhac.ddd.domain.otp;

import com.vecanhac.ddd.domain.model.enums.OtpType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OtpCodeRepository extends JpaRepository<OtpCodeEntity, Long> {

    @Query("SELECT o FROM OtpCodeEntity o WHERE o.emailOrPhone = :emailOrPhone AND o.code = :code AND o.type = :type")
    Optional<OtpCodeEntity> findByEmailOrPhoneAndCodeAndType(
            @Param("emailOrPhone") String emailOrPhone,
            @Param("code") String code,
            @Param("type") OtpType type
    );



    @Query("SELECT o FROM OtpCodeEntity o WHERE o.emailOrPhone = :email AND o.code = :code AND o.type = :type AND o.expiresAt > :now AND o.isUsed = false")
    Optional<OtpCodeEntity> findValidOtp(@Param("email") String email, @Param("code") String code, @Param("type") OtpType type, @Param("now") LocalDateTime now);

}