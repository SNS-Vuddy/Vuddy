package com.edu.ssafy.feed.model.service;

import com.edu.ssafy.feed.model.entity.User;
import com.edu.ssafy.feed.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public User findByNickname(String nickname) {

        return userRepository.findByNickname(nickname);

    }
}
