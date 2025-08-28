package com.loopers.application.coupon;

import com.loopers.domain.coupon.IssuedCoupon;
import com.loopers.domain.coupon.IssuedCouponService;
import com.loopers.domain.shared.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class IssuedCouponFacade {

    private final IssuedCouponService issuedCouponService;
    private final DomainEventPublisher domainEventPublisher;

    @Transactional
    public void use(Long issuedCouponId) {
        IssuedCoupon issuedCoupon = issuedCouponService.findById(issuedCouponId);
        issuedCoupon.use();
        domainEventPublisher.publishEvent(issuedCoupon.events());
    }

    @Transactional
    public void cancel(Long issuedCouponId) {
        IssuedCoupon issuedCoupon = issuedCouponService.findById(issuedCouponId);
        issuedCoupon.cancel();
        domainEventPublisher.publishEvent(issuedCoupon.events());
    }
}
