package com.loopers.domain.coupon;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class IssuedCouponService {

    private final IssuedCouponRepository issuedCouponRepository;

    @Transactional(readOnly = true)
    public IssuedCoupon findById(Long couponId) {
        return issuedCouponRepository.findById(couponId)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND,
                "IssuedCouponService.findById(): 쿠폰을 찾을 수 없습니다. 쿠폰 ID: " + couponId));
    }

    @Transactional(readOnly = true)
    public Optional<IssuedCoupon> findByIdOptional(Long couponId) {
        return issuedCouponRepository.findById(couponId);
    }

    @Transactional
    public IssuedCoupon findByIdWithLock(Long couponId) {
        return issuedCouponRepository.findByIdWithLock(couponId)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND,
                "IssuedCouponService.findByIdWithLock(): 쿠폰을 찾을 수 없습니다. 쿠폰 ID: " + couponId));
    }

}
