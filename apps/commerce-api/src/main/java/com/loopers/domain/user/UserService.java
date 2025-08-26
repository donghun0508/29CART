package com.loopers.domain.user;

import static com.loopers.support.error.ErrorType.NOT_FOUND;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User create(User user) {
        try {
            if (userRepository.existsByAccountIdAndEmail(user.getAccountId(), user.getEmail())) {
                log.warn("동일한 회원의 존재합니다. : {}", user.getAccountId());
                throw new CoreException(ErrorType.CONFLICT);
            }

            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            log.warn("회원 가입 중 데이터 무결성 위반 발생: {}", e.getMessage(), e);
            throw new CoreException(ErrorType.CONFLICT, "이미 존재하는 계정입니다.", e);
        }
    }

    @Transactional(readOnly = true)
    public User findByAccountId(AccountId accountId) {
        return userRepository.findByAccountId(accountId).orElseThrow(() -> new CoreException(NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Optional<User> lookupByAccountId(AccountId accountId) {
        return userRepository.findByAccountId(accountId);
    }

    @Transactional
    public User findByAccountIdWithLock(AccountId accountId) {
        return userRepository.findByAccountIdWithLock(accountId).orElseThrow(() -> new CoreException(NOT_FOUND));
    }

    @Transactional
    public User findByIdWithLock(Long userId) {
        return userRepository.findByIdWithLock(userId)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND,
                "UserService.findByIdWithLock(): 사용자를 찾을 수 없습니다. 사용자 ID: " + userId));
    }

    @Transactional
    public List<User> findAllByIdIn(List<Long> userIds) {
        return userRepository.findAllByIdIn(userIds);
    }

    @Transactional(readOnly = true)
    public User findById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(
                () -> new CoreException(ErrorType.NOT_FOUND, "UserService.findById(): 사용자를 찾을 수 없습니다. 사용자 ID: " + userId));
    }
}
