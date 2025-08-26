package com.loopers.domain.user;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByAccountId(AccountId accountId);

    boolean existsByAccountIdAndEmail(AccountId accountId, Email email);

    Optional<User> findByAccountIdWithLock(AccountId accountId);

    List<User> findAllByIdIn(Collection<Long> ids);

    Optional<User> findByIdWithLock(Long userId);
}
