package com.loopers.infrastructure.persistence.heart;

import com.loopers.domain.heart.Heart;
import com.loopers.domain.heart.Target;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartJpaRepository extends JpaRepository<Heart, Long> {

    Optional<Heart> findByUserIdAndTarget(Long userId, Target target);

    boolean existsByUserIdAndTarget(Long userId, Target target);
}
