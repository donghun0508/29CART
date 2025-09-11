package com.loopers.event.outbox.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxRepository extends JpaRepository<Outbox, Long> {

    List<Outbox> findAllByStatusIn(List<OutboxStatus> statuses);

    Optional<Outbox> findByEventId(String eventId);
}
