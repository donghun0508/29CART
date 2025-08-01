package com.loopers.domain.command.fixture;

import static org.instancio.Select.all;
import static org.instancio.Select.field;

import com.loopers.domain.command.shared.Money;
import com.loopers.domain.command.user.Point;
import com.loopers.domain.command.user.User;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.instancio.Instancio;
import org.instancio.InstancioApi;
import org.instancio.Scope;
import org.instancio.Select;

public class UserFixture {

    public static UserBuilder integration() {
        return new UserBuilder()
            .withIdClear()
            .withZeroPoint();
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder {

        private InstancioApi<User> api;

        public UserBuilder() {
            this.api = Instancio.of(User.class)
                .generate(field(User::getUserId), gen -> gen.string()
                    .length(5, 10)
                    .alphaNumeric()
                )
                .generate(field(User::getEmail), gen -> gen.net().email())
                .generate(field(User::getBirth), gen -> gen.temporal()
                    .localDate()
                    .range(LocalDate.of(1950, 1, 1), LocalDate.of(2005, 12, 31))
                    .as(date -> date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                )
                .onComplete(all(User.class), (User user) -> {
                    try {
                        Field pointField = User.class.getDeclaredField("point");
                        pointField.setAccessible(true);
                        Point pointEntity = (Point) pointField.get(user);

                        if (pointEntity != null) {
                            Field userField = Point.class.getDeclaredField("user");
                            userField.setAccessible(true);
                            userField.set(pointEntity, user);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException();
                    }
                })
            ;
        }

        public UserBuilder withPointBalance(long amount) {
            Scope balance = Select.field(Point::getBalance).toScope();
            this.api = this.api.set(field(Point::getBalance).within(balance), new Money(amount));
            return this;
        }

        public UserBuilder withIdClear() {
            this.api = this.api.set(field(User::getId), null);
            return this;
        }

        public UserBuilder withZeroPoint() {
            Scope balance = Select.field(Point::getBalance).toScope();
            this.api = this.api.set(field(Point::getBalance).within(balance), Money.ZERO);
            return this;
        }

        public User build() {
            return this.api.create();
        }
    }
}
