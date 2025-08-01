package com.loopers.domain.command.fixture;

import static org.instancio.Select.field;

import com.loopers.domain.command.shared.Money;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

public class MoneyFixture {

    public static Money complete() {
        return new MoneyBuilder().build();
    }

    public static Money zero() {
        return new MoneyBuilder().zero();
    }

    public static Money with(long value) {
        return new MoneyBuilder().with(value).build();
    }

    public static MoneyBuilder builder() {
        return new MoneyBuilder();
    }

    public static class MoneyBuilder {

        static final Long MIN_TEST_AMOUNT = 1L;
        static final Long MAX_TEST_AMOUNT = 1_000_000L;

        InstancioApi<Money> api;

        MoneyBuilder() {
            this.api = Instancio.of(Money.class)
                .generate(field(Money::value), gen -> gen.longs().range(MIN_TEST_AMOUNT, MAX_TEST_AMOUNT));
        }

        MoneyBuilder with(long amount) {
            this.api = this.api.set(field(Money::value), amount);
            return this;
        }

        Money zero() {
            return with(0L).build();
        }

        public Money build() {
            return this.api.create();
        }
    }
}
