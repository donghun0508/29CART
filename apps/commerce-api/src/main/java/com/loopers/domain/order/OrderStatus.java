package com.loopers.domain.order;

public enum OrderStatus {
    PENDING,             // 주문 대기
    PROCESSING,          // 결제 처리 중 (PG 연동 대기)
    COMPLETED,           // 주문 완료
    FAILED,              // 주문 실패 (결제 실패 등)
    CANCELLED            // 주문 취소
}
