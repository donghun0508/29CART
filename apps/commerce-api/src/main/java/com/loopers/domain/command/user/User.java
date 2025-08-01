package com.loopers.domain.command.user;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.command.shared.Money;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

@Getter
@Entity
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_USER_USER_ID", columnNames = {"user_id"}),
        @UniqueConstraint(name = "UK_USER_EMAIL", columnNames = {"email"})
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Column(
        name = "user_id",
        unique = true,
        nullable = false
    )
    @NaturalId
    private String userId;

    @Column(
        name = "email",
        unique = true,
        nullable = false
    )
    private String email;

    @Column(
        name = "birth",
        nullable = false
    )
    private String birth;

    @Enumerated(EnumType.STRING)
    @Column(
        name = "gender"
    )
    private Gender gender;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Point point;

    public static User of(UserCommand.Create command) {
        validate(command);

        User user = new User();
        user.userId = command.userId();
        user.email = command.email();
        user.birth = command.birth();
        user.gender = command.gender();
        user.point = Point.initial(user);

        return user;
    }

    public void usePoints(Money amount) {
        this.point.deduct(amount);
    }

    public void earnPoints(Money amount) {
        this.point.credit(amount);
    }

    public Money getAvailablePoints() {
        return this.point.getBalance();
    }

    private static void validate(UserCommand.Create command) {
        Validator.userId(command.userId());
        Validator.email(command.email());
        Validator.birth(command.birth());
        Validator.gender(command.gender());
    }

    private static class Validator {

        private static final Pattern USER_ID_REGEX = Pattern.compile("^[a-zA-Z0-9]{1,10}$");
        private static final Pattern EMAIL_REGEX = Pattern.compile(
            "^[a-zA-Z0-9](?:[a-zA-Z0-9_-]*[a-zA-Z0-9]|[a-zA-Z0-9_-]*\\.[a-zA-Z0-9](?:[a-zA-Z0-9_-]*[a-zA-Z0-9])*)*@[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9]|[a-zA-Z0-9-]*\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])*)*\\.[a-zA-Z]{2,6}$"
        );
        private static final Pattern BIRTH_REGEX = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");

        private Validator() {
        }

        public static void userId(String userId) {
            if (isNullOrBlank(userId) ||
                !USER_ID_REGEX.matcher(userId).matches()) {
                throw new CoreException(ErrorType.INVALID_INPUT);
            }
        }

        public static void email(String email) {
            if (isNullOrBlank(email) ||
                !EMAIL_REGEX.matcher(email).matches()
            ) {
                throw new CoreException(ErrorType.INVALID_INPUT);
            }
        }

        public static void birth(String birth) {
            validateBirthFormat(birth);
            validateBirthDate(birth);
        }

        public static void gender(Gender gender) {
            if (gender == null) {
                throw new CoreException(ErrorType.INVALID_INPUT);
            }
        }

        private static boolean isNullOrBlank(String value) {
            return value == null || value.isBlank();
        }

        private static void validateBirthFormat(String birth) {
            if (isNullOrBlank(birth) ||
                !BIRTH_REGEX.matcher(birth).matches()
            ) {
                throw new CoreException(ErrorType.INVALID_INPUT);
            }
        }

        private static void validateBirthDate(String birth) {
            try {
                LocalDate.parse(birth);
            } catch (DateTimeParseException e) {
                throw new CoreException(ErrorType.INVALID_INPUT);
            }
        }
    }

    public enum Gender {
        M, F;
    }
}
