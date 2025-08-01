package com.loopers.domain.command.heart;

import java.util.List;
import java.util.Optional;

public interface HeartRepository {

    Heart save(Heart heart);

    void delete(Heart heart);

    void deleteByUserIdAndTarget(Long userId, Target target);

    Optional<Heart> findByUserIdAndTarget(Long userId, Target target);

    boolean existsByUserIdAndTarget(Long userId, Target target);

    List<Heart> findAllByUserIdAndTargetType(Long userId, TargetType targetType);
}
