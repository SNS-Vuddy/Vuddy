package com.buddy.feed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.buddy.feed.domain.Feed;
import java.util.Optional;

public interface FeedCreateRepository extends JpaRepository<Feed, Long> {
    Optional<Feed> findByNickname(String nickname);
}
