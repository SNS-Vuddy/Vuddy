package com.buddy.service;

import com.buddy.model.entity.User;
import com.buddy.repository.TestRepository;
import com.buddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TestRepository testRepository;

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
        User findUser = testRepository.findByNickname(user.getNickname());
        if (findUser != null) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    public User findByNickname(String nickname) {
        return testRepository.findByNickname(nickname);
    }

    @Transactional
    public void changeUserStatusMessage(String userNickname, String statusMessage) {
        User user = testRepository.findByNickname(userNickname);
        System.out.println("==============================================================================");
        System.out.println("statusMessage = " + statusMessage);
        System.out.println("==============================================================================");
        user.updateStatusMessage(statusMessage);
    }

}
