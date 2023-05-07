package com.edu.ssafy.friend.model.repository;

import com.edu.ssafy.friend.model.entity.TaggedFriends;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaggedFriendsRepository extends JpaRepository<TaggedFriends, Long> {
}
