package com.loopers.event.outbox.support;

@FunctionalInterface
public interface SafeCloseable extends AutoCloseable {

    @Override
    void close(); // throws 없음
}
