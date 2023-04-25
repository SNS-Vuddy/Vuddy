package com.buddy.feed.service;

import com.buddy.feed.domain.Feed;
import com.buddy.feed.repository.FeedSearchRepository;
import com.buddy.feed.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.buddy.feed.dto.FeedSearchDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import com.buddy.feed.repository.FeedCreateRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 이 어노테이션을 붙이면 이 class 안에 있는 모든 메서드는 데이터를 읽기 전용으로 불러온다.
public class FeedService {
    private final FeedCreateRepository feedCreateRepository;

    @Autowired
    private FeedSearchRepository feedSearchRepository;

    public List<String> getUserStartWith(String user) {
        List<String> feedSearchDTOList = new ArrayList<>();
        for (User eachUser : feedSearchRepository.findByNicknameStartsWith(user)) {
            feedSearchDTOList.add(eachUser.getNickname());
        }
        return feedSearchDTOList;
    }

    @Transactional
    public void createFeed() {
        Feed feed = new Feed(1L,"11", "1111","asdfsaf");
        feedCreateRepository.save(feed);
    }
}
