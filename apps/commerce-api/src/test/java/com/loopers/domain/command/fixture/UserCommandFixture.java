package com.loopers.domain.command.fixture;

import static org.instancio.Select.field;

import com.loopers.domain.command.user.UserCommand;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

public class UserCommandFixture {

    public static class Create {

        public static InstancioApi<UserCommand.Create> complete() {
            return Instancio.of(UserCommand.Create.class)
                .generate(field(UserCommand.Create::userId), gen -> gen.string()
                    .length(5, 10)
                    .alphaNumeric()
                )
                .generate(field(UserCommand.Create::email), gen -> gen.net().email())
                .generate(field(UserCommand.Create::birth), gen -> gen.temporal()
                    .localDate()
                    .range(LocalDate.of(1950, 1, 1), LocalDate.of(2005, 12, 31))
                    .as(date -> date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                );
        }
    }
}
