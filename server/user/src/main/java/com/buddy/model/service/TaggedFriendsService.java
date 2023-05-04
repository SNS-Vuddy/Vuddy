package com.buddy.model.service;

import com.buddy.model.entity.TaggedFriends;
import com.buddy.model.repository.TaggedFriendsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaggedFriendsService {

    private final TaggedFriendsRepository taggedFriendsRepository;

    @Transactional
    public void saveTaggedFriends(TaggedFriends taggedFriends) {
        taggedFriendsRepository.save(taggedFriends);
    }
}
