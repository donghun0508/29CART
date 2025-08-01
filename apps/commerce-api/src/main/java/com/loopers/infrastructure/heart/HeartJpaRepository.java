package com.loopers.infrastructure.heart;

import com.loopers.domain.command.heart.Heart;
import com.loopers.domain.command.heart.Target;
import com.loopers.domain.command.heart.TargetType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HeartJpaRepository extends JpaRepository<Heart, Long>, CustomHeartQueryRepository {

    boolean existsByUserIdAndTarget(Long userId, Target target);

    Optional<Heart> findByUserIdAndTarget(Long userId, Target target);

    @Query("SELECT h FROM Heart h WHERE h.userId = :userId AND h.target.targetType = :targetType")
    List<Heart> findAllByUserIdAndTargetType(@Param("userId") Long userId, @Param("targetType") TargetType targetType);

    void deleteByUserIdAndTarget(Long userId, Target target);
}
