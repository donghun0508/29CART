package com.loopers.fixture;

import com.loopers.application.user.UserCommand.UserRegisterCommand;
import com.loopers.domain.user.Gender;
import org.instancio.Instancio;
import org.instancio.InstancioApi;
import org.instancio.settings.AssignmentType;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.instancio.Select.field;

public class UserRegisterCommandFixture {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final InstancioApi<UserRegisterCommand> api;

        public Builder() {
            this.api = Instancio.of(UserRegisterCommand.class)
                    .withSettings(Settings.create().set(Keys.ASSIGNMENT_TYPE, AssignmentType.METHOD))
                    .supply(field(UserRegisterCommand::accountId), () ->
                            Instancio.gen().string().length(5, 10).alphaNumeric().get())
                    .supply(field(UserRegisterCommand::email), () ->
                           Instancio.gen().net().email().get())
                    .supply(field(UserRegisterCommand::birth), () -> {
                        LocalDate randomDate = Instancio.gen().temporal()
                                .localDate()
                                .range(LocalDate.of(1950, 1, 1), LocalDate.of(2005, 12, 31))
                                .get();
                        return randomDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    })
                    .supply(field(UserRegisterCommand::gender), () ->
                            Instancio.gen().enumOf(Gender.class).get());
        }

        public Builder accountId(String accountId) {
            this.api.set(field(UserRegisterCommand::accountId), accountId);
            return this;
        }

        public Builder email(String email) {
            this.api.set(field(UserRegisterCommand::email), email);
            return this;
        }

        public Builder birth(String birth) {
            this.api.set(field(UserRegisterCommand::birth), birth);
            return this;
        }

        public Builder gender(Gender gender) {
            this.api.set(field(UserRegisterCommand::gender), gender);
            return this;
        }

        public UserRegisterCommand build() {
            return api.create();
        }
    }
}
