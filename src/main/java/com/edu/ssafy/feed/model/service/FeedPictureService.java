package com.edu.ssafy.feed.model.service;

import com.edu.ssafy.feed.model.entity.FeedPictures;
import com.edu.ssafy.feed.model.repository.FeedPictureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedPictureService {

    private final FeedPictureRepository feedPictureRepository;

    @Transactional
    public void saveAll(List<FeedPictures> feedPictures) {
        feedPictureRepository.saveAll(feedPictures);
    }
}
