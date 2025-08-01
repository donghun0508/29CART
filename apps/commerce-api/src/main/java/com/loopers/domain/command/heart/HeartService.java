package com.loopers.domain.command.heart;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class HeartService {

    private final HeartRepository heartRepository;

    @Transactional
    public Heart create(HeartCommand.Create command) {
        Heart heart = Heart.create(command);
        return heartRepository.save(heart);
    }

    @Transactional
    public void deleteByUserIdAndTarget(Long userId, Target target) {
        heartRepository.deleteByUserIdAndTarget(userId, target);
    }

    @Transactional(readOnly = true)
    public boolean existsByUserIdAndTarget(Long userId, Target target) {
        return heartRepository.existsByUserIdAndTarget(userId, target);
    }

    @Transactional(readOnly = true)
    public List<Heart> findAllByUserIdAndTargetType(Long userId, TargetType targetType) {
        return heartRepository.findAllByUserIdAndTargetType(userId, targetType);
    }
}
