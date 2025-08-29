package com.loopers.fixture;

import static org.instancio.Select.field;

import com.loopers.domain.coupon.Issuance;
import com.loopers.domain.coupon.TargetScope;
import java.time.ZonedDateTime;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

public class IssuanceFixture {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final InstancioApi<Issuance> api;

        public Builder() {
            ZonedDateTime now = ZonedDateTime.now();
            ZonedDateTime oneYearLater = now.plusYears(1);

            this.api = Instancio.of(Issuance.class)
                .generate(field(Issuance::issuedAt), gen -> gen.temporal()
                    .zonedDateTime()
                    .range(now.minusDays(30), now))
                .generate(field(Issuance::expiredAt), gen -> gen.temporal()
                    .zonedDateTime()
                    .range(now.plusDays(1), oneYearLater));
        }

        public Builder targetScope(TargetScope targetScope) {
            this.api.set(field(Issuance::targetScope), targetScope);
            return this;
        }

        public Builder targetId(Long targetId) {
            this.api.set(field(Issuance::targetId), targetId);
            return this;
        }

        public Builder issuedAt(ZonedDateTime issuedAt) {
            this.api.set(field(Issuance::issuedAt), issuedAt);
            return this;
        }

        public Builder expiredAt(ZonedDateTime expiredAt) {
            this.api.set(field(Issuance::expiredAt), expiredAt);
            return this;
        }

        public Issuance build() {
            return api.create();
        }
    }

}
