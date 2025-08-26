package com.loopers.domain.coupon;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface IssuedCouponRepository extends Repository<IssuedCoupon, Long> {

    Optional<IssuedCoupon> findById(Long couponId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ic FROM IssuedCoupon ic WHERE ic.id = :couponId")
    Optional<IssuedCoupon> findByIdWithLock(@Param("couponId") Long couponId);
}
