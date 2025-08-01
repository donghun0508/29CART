package com.loopers.domain.command.order;

import com.loopers.domain.command.shared.Money;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class OrderLines {

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLine> lines;

    private OrderLines(List<OrderLine> lines) {
        this.lines = new ArrayList<>(lines);
    }

    public static OrderLines of(List<OrderLine> orderLines) {
        return new OrderLines(orderLines);
    }

    public static OrderLines empty() {
        return new OrderLines(new ArrayList<>());
    }

    void add(OrderLine orderLine) {
        validateDuplicateProduct(orderLine);
        this.lines.add(orderLine);
    }

    Map<Long, Long> map() {
        return this.lines.stream()
            .map(OrderLine::toPickedItemEntry)
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue
            ));
    }

    Money calculateTotalPrice() {
        return lines.stream()
            .map(OrderLine::calculateLineTotal)
            .reduce(Money.ZERO, Money::add);
    }

    int size() {
        return this.lines.size();
    }

    private void validateDuplicateProduct(OrderLine orderLine) {
        boolean exists = lines.stream().anyMatch(line -> line.hasSameProduct(orderLine));

        if (exists) {
            throw new IllegalStateException("이미 동일한 상품이 주문 목록에 존재합니다. " + orderLine);
        }
    }
}
