package com.edu.ssafy.feed.model.repository;

import com.edu.ssafy.feed.model.entity.Feed;
import com.edu.ssafy.feed.model.entity.FeedLikes;
import com.edu.ssafy.feed.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FeedLikesRepository extends JpaRepository<FeedLikes, Long> {

    @Query("select f from FeedLikes f where f.feed = :feed and f.user = :user")
    Optional<FeedLikes> findByFeedAndUser(@Param("feed") Feed feed, @Param("user") User user);

}
