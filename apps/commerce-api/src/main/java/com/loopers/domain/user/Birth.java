package com.loopers.domain.user;

import static com.loopers.domain.shared.Preconditions.requireNonBlank;

import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

@Embeddable
public record Birth(String day) {

    private static final Pattern BIRTH_REGEX = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");

    public Birth {
        requireNonBlank(day, "생년월일은 비어있을 수 없습니다.");
        validateBirthFormat(day, "생년월일 형식이 잘못되었습니다. 올바른 형식은 YYYY-MM-DD입니다.");
        requireValidLocalDate(day, "생년월일 형식이 잘못되었습니다. 올바른 형식은 YYYY-MM-DD입니다.");
    }

    public static Birth of(String day) {
        return new Birth(day);
    }

    private static void validateBirthFormat(String day, String errorMessage) {
        if (!BIRTH_REGEX.matcher(day).matches()) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private static void requireValidLocalDate(String day, String errorMessage) {
        try {
            LocalDate.parse(day);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
