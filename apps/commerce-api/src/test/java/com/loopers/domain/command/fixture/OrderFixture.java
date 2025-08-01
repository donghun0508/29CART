package com.loopers.domain.command.fixture;

import static org.instancio.Select.all;
import static org.instancio.Select.field;

import com.loopers.domain.command.order.Order;
import com.loopers.domain.command.order.OrderLine;
import com.loopers.domain.command.order.OrderLines;
import java.lang.reflect.Field;
import java.util.List;
import org.instancio.Instancio;
import org.instancio.InstancioApi;
import org.instancio.Select;

public class OrderFixture {

    public static OrderBuilder builder() {
        return new OrderBuilder();
    }

    public static class OrderBuilder {

        private InstancioApi<Order> api;

        static final Integer MIN_TEST_SIZE = 1;
        static final Integer MAX_TEST_SIZE = 10;

        public OrderBuilder() {
            this.api = Instancio.of(Order.class)
                .set(field(Order::getOrderLines), OrderLines.of(
                    Instancio.ofList(OrderLine.class)
                        .generate(Select.root(), gen -> gen.collection()
                            .minSize(MIN_TEST_SIZE)
                            .maxSize(MAX_TEST_SIZE))
                        .create()
                ))
                .onComplete(all(Order.class), (Order order) -> {
                    try {
                        Field orderLinesField = Order.class.getDeclaredField("orderLines");
                        orderLinesField.setAccessible(true);
                        OrderLines orderLinesEntity = (OrderLines) orderLinesField.get(order);

                        if (orderLinesEntity != null) {
                            Field listField = OrderLines.class.getDeclaredField("lines");
                            listField.setAccessible(true);
                            List<OrderLine> orderLineList = (List<OrderLine>) listField.get(orderLinesEntity);

                            if (orderLineList != null) {
                                for (OrderLine orderLine : orderLineList) {
                                    Field orderField = OrderLine.class.getDeclaredField("order");
                                    orderField.setAccessible(true);
                                    orderField.set(orderLine, order);
                                }
                            }
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        }

        public OrderBuilder withOrderLineSize(int size) {
            List<OrderLine> orderLineList = Instancio.ofList(OrderLine.class)
                .size(size)
                .create();

            this.api = this.api.set(field(Order::getOrderLines), OrderLines.of(orderLineList));
            return this;
        }

        public OrderBuilder withEmptyOrderLine() {
            return withOrderLineSize(0);
        }

        public Order build() {
            return this.api.create();
        }

    }
}
