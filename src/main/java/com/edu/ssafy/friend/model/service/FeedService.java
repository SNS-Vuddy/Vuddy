package com.edu.ssafy.friend.model.service;

import com.edu.ssafy.friend.model.entity.Feed;
import com.edu.ssafy.friend.model.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedService {

    private final FeedRepository feedRepository;

}
