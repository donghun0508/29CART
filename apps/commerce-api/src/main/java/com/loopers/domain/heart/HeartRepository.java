package com.loopers.domain.heart;

import java.util.Optional;

public interface HeartRepository {

    Heart save(Heart heart);

    Optional<Heart> findByUserIdAndTarget(Long userId, Target target);

    long count();

    void delete(Heart heart);

    boolean existsByUserIdAndTarget(Long userId, Target target);
}
