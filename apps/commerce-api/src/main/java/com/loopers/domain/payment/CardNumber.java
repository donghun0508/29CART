package com.loopers.domain.payment;

import static com.loopers.domain.shared.Preconditions.requireNonBlank;

import java.util.regex.Pattern;

public record CardNumber(String number) {

    private static final Pattern CARD_NUMBER_PATTERN = Pattern.compile("^\\d{16}$");

    public CardNumber {
        requireNonBlank(number, "카드 번호는 비어있을 수 없습니다.");
        requireValidFormat(number, "카드 번호는 16자리 숫자여야 합니다.");
    }

    private static void requireValidFormat(String value, String errorMessage) {
        String cleanNumber = value.replace("-", "");
        if (!CARD_NUMBER_PATTERN.matcher(cleanNumber).matches()) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static CardNumber of(String cardNumber) {
        return new CardNumber(cardNumber);
    }
}
