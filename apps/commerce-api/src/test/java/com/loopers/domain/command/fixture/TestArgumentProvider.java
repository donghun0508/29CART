package com.loopers.domain.command.fixture;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class TestArgumentProvider {

    public static Stream<Integer> generateRandomCount() {
        return Stream.of(ThreadLocalRandom.current().nextInt(1, 21));
    }

    public static Stream<Long> generateZeroOrNull() {
        return Stream.of(0L, null);
    }
}
