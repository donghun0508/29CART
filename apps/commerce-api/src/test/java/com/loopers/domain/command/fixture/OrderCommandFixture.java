package com.loopers.domain.command.fixture;

import static org.instancio.Select.field;

import com.loopers.domain.command.order.OrderCommand;
import com.loopers.domain.command.order.OrderCommand.Create.OrderItem;
import java.util.List;
import org.instancio.Instancio;
import org.instancio.InstancioApi;
import org.instancio.Select;

public class OrderCommandFixture {

    public static class Create {

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {

            private InstancioApi<OrderCommand.Create> api;

            static final Integer MIN_TEST_SIZE = 1;
            static final Integer MAX_TEST_SIZE = 10;

            public Builder() {
                this.api = Instancio.of(OrderCommand.Create.class)
                    .set(field(OrderCommand.Create::items),
                        Instancio.ofList(OrderItem.class)
                            .generate(Select.root(), gen -> gen.collection()
                                .minSize(MIN_TEST_SIZE)
                                .maxSize(MAX_TEST_SIZE))
                            .generate(field(OrderItem::productId), gen -> gen.longs().min(1L).max(1000L))
                            .generate(field(OrderItem::price), gen -> gen.longs().min(1000L).max(100000L))
                            .generate(field(OrderItem::quantity), gen -> gen.longs().min(1L).max(10L))
                            .create()
                    )
                ;
            }

            public Builder withOrderItemEmpty() {
                return withOrderItems(0);
            }

            public Builder withOrderItems(int size) {
                List<OrderItem> orderItems = Instancio.ofList(OrderItem.class)
                    .size(size)
                    .generate(field(OrderItem::productId), gen -> gen.longs().min(1L).max(1000L))
                    .generate(field(OrderItem::price), gen -> gen.longs().min(1000L).max(100000L))
                    .generate(field(OrderItem::quantity), gen -> gen.longs().min(1L).max(10L))
                    .create();

                this.api = this.api.set(field(OrderCommand.Create::items), orderItems);
                return this;
            }

            public Builder withUserId(Long userId) {
                this.api =  this.api.set(field(OrderCommand.Create::userId), userId);
                return this;
            }

            public OrderCommand.Create build() {
                return this.api.create();
            }
        }
    }
}
