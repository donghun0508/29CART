package com.loopers.domain.user;

import static com.loopers.domain.shared.Preconditions.requireNonBlank;

import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;

@Embeddable
public record AccountId(String value) {

    private static final Pattern ACCOUNT_ID_REGEX = Pattern.compile("^[a-zA-Z0-9]{1,10}$");

    public AccountId {
        requireNonBlank(value, "계정 ID는 비어있을 수 없습니다.");
        requireValidFormat(value, "계정 ID는 1~10자의 영문 대소문자 또는 숫자만 포함할 수 있습니다.");
    }

    public static AccountId of(String value) {
        return new AccountId(value);
    }

    private static void requireValidFormat(String value, String errorMessage) {
        if (!ACCOUNT_ID_REGEX.matcher(value).matches()) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
