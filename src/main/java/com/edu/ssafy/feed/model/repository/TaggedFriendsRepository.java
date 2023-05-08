package com.edu.ssafy.feed.model.repository;

import com.edu.ssafy.feed.model.entity.TaggedFriends;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaggedFriendsRepository extends JpaRepository<TaggedFriends, Long> {
    void deleteByFeedId(Long feedId);

}
