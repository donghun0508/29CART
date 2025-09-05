package com.loopers.service;

import com.loopers.domain.EventLog;
import com.loopers.repository.EventLogRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EventLogService {

    private final EventLogRepository eventLogRepository;

    public void saveAll(List<EventLog> logs) {
        eventLogRepository.saveAll(logs);
    }
}
