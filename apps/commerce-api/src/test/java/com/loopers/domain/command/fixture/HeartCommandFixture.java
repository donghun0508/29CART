package com.loopers.domain.command.fixture;

import static org.instancio.Select.field;

import com.loopers.domain.command.heart.HeartCommand;
import com.loopers.domain.command.heart.Target;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

public class HeartCommandFixture {

    public static class Create {

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {

            private final InstancioApi<HeartCommand.Create> api;

            public Builder() {
                this.api = Instancio.of(HeartCommand.Create.class);
            }

            public Builder withUserId(Long userId) {
                this.api.set(field(HeartCommand.Create::userId), userId);
                return null;
            }

            public Builder withTarget(Target target) {
                this.api.set(field(HeartCommand.Create::target), target);
                return this;
            }

            public HeartCommand.Create build() {
                return this.api.create();
            }
        }
    }
}
