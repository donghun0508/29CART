package com.loopers.infrastructure.persistence.user;

import com.loopers.domain.user.AccountId;
import com.loopers.domain.user.Email;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserRepository;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id);
    }

    @Override
    public Optional<User> findByAccountId(AccountId accountId) {
        return userJpaRepository.findByAccountId(accountId);
    }

    @Override
    public boolean existsByAccountIdAndEmail(AccountId accountId, Email email) {
        return userJpaRepository.existsByAccountIdAndEmail(accountId, email);
    }

    @Override
    public Optional<User> findByAccountIdWithLock(AccountId accountId) {
        return userJpaRepository.findByAccountIdWithLock(accountId);
    }

    @Override
    public List<User> findAllByIdIn(Collection<Long> ids) {
        return userJpaRepository.findAllByIdIn(ids);
    }

    @Override
    public Optional<User> findByIdWithLock(Long userId) {
        return userJpaRepository.findByIdWithLock(userId);
    }
}
