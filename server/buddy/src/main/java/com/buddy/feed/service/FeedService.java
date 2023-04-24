package com.buddy.feed.service;

import com.buddy.feed.repository.FeedSearchRepository;
import com.buddy.feed.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.buddy.feed.dto.FeedSearchDTO;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeedService {

    @Autowired
    private FeedSearchRepository feedSearchRepository;

    public List<String> getUserStartWith(String user) {
        List<String> feedSearchDTOList = new ArrayList<>();
        for (User eachUser : feedSearchRepository.findByNicknameStartsWith(user)) {
            feedSearchDTOList.add(eachUser.getNickname());
        }
        return feedSearchDTOList;
    }
}
