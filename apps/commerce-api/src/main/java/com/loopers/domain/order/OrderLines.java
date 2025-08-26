package com.loopers.domain.order;

import static com.loopers.domain.shared.Preconditions.requireNonEmpty;
import static java.util.stream.Collectors.toList;

import com.loopers.domain.shared.Money;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter(AccessLevel.PACKAGE)
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class OrderLines {

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLine> lines;

    private OrderLines(List<OrderLine> lines) {
        this.lines = new ArrayList<>(lines);
    }

    static OrderLines of(Order order, List<OrderItem> orderItems) {
        requireNonEmpty(orderItems, "주문 항목은 비어있을 수 없습니다.");
        return new OrderLines(orderItems.stream().map(item -> OrderLine.from(order, item)).toList());
    }

    Money calculateTotalAmount() {
        return lines.stream()
            .map(OrderLine::calculateLineTotal)
            .reduce(Money.ZERO, Money::add);
    }
}
