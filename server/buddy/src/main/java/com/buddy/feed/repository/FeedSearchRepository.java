package com.buddy.feed.repository;

import java.util.List;
import java.util.Date;

import com.buddy.feed.domain.Feed;
import com.buddy.feed.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface FeedSearchRepository extends JpaRepository<User, Long> {
    List<User> findByNicknameStartsWith(String nickname);
}