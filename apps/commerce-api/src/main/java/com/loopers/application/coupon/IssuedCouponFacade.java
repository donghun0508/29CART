package com.loopers.application.coupon;

import com.loopers.domain.coupon.IssuedCoupon;
import com.loopers.domain.coupon.IssuedCouponService;
import com.loopers.event.outbox.EventStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class IssuedCouponFacade {

    private final IssuedCouponService issuedCouponService;
    private final EventStore eventStore;

    @Transactional
    public void use(Long issuedCouponId) {
        IssuedCoupon issuedCoupon = issuedCouponService.findById(issuedCouponId);
        issuedCoupon.use();
        eventStore.save(issuedCoupon);
    }

    @Transactional
    public void cancel(Long issuedCouponId) {
        IssuedCoupon issuedCoupon = issuedCouponService.findById(issuedCouponId);
        issuedCoupon.cancel();
        eventStore.save(issuedCoupon);
    }
}
