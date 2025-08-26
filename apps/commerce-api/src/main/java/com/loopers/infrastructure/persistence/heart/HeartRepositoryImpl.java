package com.loopers.infrastructure.persistence.heart;

import com.loopers.domain.heart.Heart;
import com.loopers.domain.heart.HeartRepository;
import com.loopers.domain.heart.Target;
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
    public Optional<Heart> findByUserIdAndTarget(Long userId, Target target) {
        return heartJpaRepository.findByUserIdAndTarget(userId, target);
    }

    @Override
    public long count() {
        return heartJpaRepository.count();
    }

    @Override
    public void delete(Heart heart) {
        heartJpaRepository.delete(heart);
    }

    @Override
    public boolean existsByUserIdAndTarget(Long userId, Target target) {
        return heartJpaRepository.existsByUserIdAndTarget(userId, target);
    }
}
