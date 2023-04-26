package com.buddy.service;

import com.buddy.model.entity.User;
import com.buddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

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
        // EXCEPTION
        if (!userRepository.findByNickname(user.getNickname()).getNickname().isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    public User findByNickname(String userNickname) {
        return userRepository.findByNickname(userNickname);
    }

    public void changeUserStatusMessage(String userNickname, String statusMessage) {
        User user = userRepository.updateStatusMessage(userNickname, statusMessage);
        user.setStatusMessage(statusMessage);
    }
}
