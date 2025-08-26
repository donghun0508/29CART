package com.loopers.infrastructure.persistence.user;

import com.loopers.domain.user.AccountId;
import com.loopers.domain.user.Email;
import com.loopers.domain.user.User;
import jakarta.persistence.LockModeType;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u JOIN FETCH u.point WHERE u.accountId = :accountId")
    Optional<User> findByAccountId(@Param("accountId") AccountId accountId);

    boolean existsByAccountIdAndEmail(AccountId accountId, Email email);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM User u WHERE u.accountId = :accountId")
    Optional<User> findByAccountIdWithLock(@Param("accountId") AccountId accountId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM User u WHERE u.id = :userId")
    Optional<User> findByIdWithLock(@Param("userId") Long userId);

    List<User> findAllByIdIn(Collection<Long> ids);
}
