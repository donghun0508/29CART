package com.loopers.domain.command.user;

import com.loopers.support.error.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User create(UserCommand.Create command) {
        User user = User.of(command);
        return userRepository.save(user);
    }

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findByUserId(String userId) {
        return userRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public boolean existByUserId(String userId) {
        return userRepository.findByUserId(userId).isPresent();
    }
}
