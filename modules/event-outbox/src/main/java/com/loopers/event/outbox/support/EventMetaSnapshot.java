package com.loopers.event.outbox.support;

public record EventMetaSnapshot(String correlationId, String parentEventId, String rootAggregateId) {

}
