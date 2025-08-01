package com.loopers.domain.command.order;

import static com.loopers.utils.ValidationUtils.requireNonNull;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.command.order.OrderCommand.Create.OrderItem;
import com.loopers.domain.command.shared.Money;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Column(
        name = "user_id",
        nullable = false,
        updatable = false
    )
    private Long userId;

    @Embedded
    private OrderLines orderLines;

    public static Order create(OrderCommand.Create command) {
        requireNonNull(command, "주문 생성 명령 요청은 null일 수 없습니다.");

        Order order = new Order();
        order.userId = command.userId();
        order.orderLines = OrderLines.empty();

        for (OrderItem item : command.items()) {
            OrderLine orderLine = OrderLine.createItem(order, item);
            order.orderLines.add(orderLine);
        }

        return order;
    }

    public Map<Long, Long> lines() {
        return this.orderLines.map();
    }

    public Money getTotalAmount() {
        return this.orderLines.calculateTotalPrice();
    }

    public int getOrderLineCount() {
        return this.orderLines.size();
    }
}
