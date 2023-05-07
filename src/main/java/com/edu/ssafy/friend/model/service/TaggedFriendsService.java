package com.edu.ssafy.friend.model.service;

import com.edu.ssafy.friend.model.repository.TaggedFriendsRepository;
import com.edu.ssafy.friend.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaggedFriendsService {

    private final TaggedFriendsRepository taggedFriendsRepository;
    private final UserRepository userRepository;


}
