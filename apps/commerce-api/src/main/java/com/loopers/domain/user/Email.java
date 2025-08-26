package com.loopers.domain.user;

import static com.loopers.domain.shared.Preconditions.requireNonBlank;

import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;

@Embeddable
public record Email(String address) {

    private static final Pattern EMAIL_REGEX = Pattern.compile(
        "^[a-zA-Z0-9](?:[a-zA-Z0-9_-]*[a-zA-Z0-9]|[a-zA-Z0-9_-]*\\.[a-zA-Z0-9](?:[a-zA-Z0-9_-]*[a-zA-Z0-9])*)*@[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9]|[a-zA-Z0-9-]*\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])*)*\\.[a-zA-Z]{2,6}$"
    );

    public Email {
        requireNonBlank(address, "이메일 주소는 비어있을 수 없습니다.");
        requireValidFormat(address, "이메일 주소 형식이 잘못되었습니다.");
    }

    public static Email of(String address) {
        return new Email(address);
    }

    private static void requireValidFormat(String address, String errorMessage) {
        if (!EMAIL_REGEX.matcher(address).matches()) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
