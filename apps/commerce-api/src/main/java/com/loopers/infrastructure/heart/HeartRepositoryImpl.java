package com.loopers.infrastructure.heart;

import com.loopers.domain.command.heart.Heart;
import com.loopers.domain.command.heart.HeartRepository;
import com.loopers.domain.command.heart.Target;
import com.loopers.domain.command.heart.TargetType;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class HeartRepositoryImpl implements HeartRepository {

    private final HeartJpaRepository heartJpaRepository;

    @Override
    public Heart save(Heart heart) {
        return heartJpaRepository.save(heart);
    }

    @Override
    public void delete(Heart heart) {
        heartJpaRepository.delete(heart);
    }

    @Override
    public void deleteByUserIdAndTarget(Long userId, Target target) {
        heartJpaRepository.deleteByUserIdAndTarget(userId, target);
    }

    @Override
    public Optional<Heart> findByUserIdAndTarget(Long userId, Target target) {
        return heartJpaRepository.findByUserIdAndTarget(userId, target);
    }

    @Override
    public boolean existsByUserIdAndTarget(Long userId, Target target) {
        return heartJpaRepository.existsByUserIdAndTarget(userId, target);
    }

    @Override
    public List<Heart> findAllByUserIdAndTargetType(Long userId, TargetType targetType) {
        return heartJpaRepository.findAllByUserIdAndTargetType(userId, targetType);
    }
}
