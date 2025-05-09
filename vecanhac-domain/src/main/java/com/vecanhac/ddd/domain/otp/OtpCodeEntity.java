package com.vecanhac.ddd.domain.otp;


import com.vecanhac.ddd.domain.model.enums.OtpType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "otp_codes")
@Data
public class OtpCodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email_or_phone", nullable = false)
    private String emailOrPhone;

    @Column(nullable = false, length = 6)
    private String code;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Enumerated(EnumType.STRING)
    private OtpType type; // register, reset

    @Column(name = "is_used", nullable = false)
    private boolean isUsed = false;
}
