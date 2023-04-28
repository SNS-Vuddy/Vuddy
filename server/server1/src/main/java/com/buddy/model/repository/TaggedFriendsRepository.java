package com.buddy.model.repository;

import com.buddy.model.entity.TaggedFriends;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaggedFriendsRepository extends JpaRepository<TaggedFriends, Long> {

    List<TaggedFriends> findAllByFeedId(Long feedId);

}