package com.buddy.model.repository;

import com.buddy.model.entity.Feed;
import com.buddy.model.entity.FeedLikes;
import com.buddy.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeedLikesRepository extends JpaRepository<FeedLikes, Long> {

    @Query("select f from FeedLikes f where f.feed = :feed and f.user = :user")
    Optional<FeedLikes> findByFeedAndUser(@Param("feed") Feed feed, @Param("user") User user);
}
