package com.edu.ssafy.feed.model.service;

import com.edu.ssafy.feed.model.dto.request.FeedWriteReq;
import com.edu.ssafy.feed.model.entity.Feed;
import com.edu.ssafy.feed.model.entity.TaggedFriends;
import com.edu.ssafy.feed.model.entity.User;
import com.edu.ssafy.feed.model.repository.TaggedFriendsRepository;
import com.edu.ssafy.feed.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaggedFriendsService {

    private final TaggedFriendsRepository taggedFriendsRepository;
    private final UserRepository userRepository;

    @Transactional
    public void saveTaggedFriends(TaggedFriends taggedFriends) {
        taggedFriendsRepository.save(taggedFriends);
    }

    @Transactional
    public void saveAllTaggedFriends(FeedWriteReq req, Feed feed) {

        List<User> taggedUsers = userRepository.findAllByNicknameIn(req.getTags());
        List<TaggedFriends> taggedFriendsList = new ArrayList<>();

        for (User taggedUser : taggedUsers) {
            TaggedFriends taggedFriends = req.toTagEntity(feed, taggedUser);
            taggedFriendsList.add(taggedFriends);
        }

        taggedFriendsRepository.saveAll(taggedFriendsList);

    }
}
