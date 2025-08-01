package com.loopers.infrastructure.user;

import com.loopers.domain.command.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    
    @Query("SELECT u FROM User u JOIN FETCH u.point WHERE u.userId = :userId")
    Optional<User> findByUserIdWithPoint(@Param("userId") String userId);
}
