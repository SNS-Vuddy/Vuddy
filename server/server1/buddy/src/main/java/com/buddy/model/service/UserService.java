package com.buddy.model.service;

import com.buddy.model.entity.User;
import com.buddy.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    private final EntityManager em;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public Long join(User user) {
        validateDuplicateUser(user); // 중복 회원 검증
        userRepository.save(user);
        return user.getId();
    }

    private void validateDuplicateUser(User user) {
        if (userRepository.findByNickname(user.getNickname()) != null) {
            throw new IllegalStateException("이 회원은 이미 존재합니다.");
        }
    }

    public User findByNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }

    public User findByUserId(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Transactional
    public void changeUserStatusMessage(Long userID, String statusMessage) {
        User user = userRepository.findById(userID).orElse(null);
        Objects.requireNonNull(user).updateStatusMessage(statusMessage);
    }

}
