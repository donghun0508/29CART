package com.loopers.domain.command.user;

import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findByUserId(String userId);
}
