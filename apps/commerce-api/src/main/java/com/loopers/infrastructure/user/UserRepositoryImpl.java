package com.loopers.infrastructure.user;

import com.loopers.domain.command.user.User;
import com.loopers.domain.command.user.UserRepository;
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
    public Optional<User> findByUserId(String userId) {
        return userJpaRepository.findByUserIdWithPoint(userId);
    }
}
