package com.edu.ssafy.feed.model.repository;

import com.edu.ssafy.feed.model.entity.Feed;
import com.edu.ssafy.feed.model.repository.custom.FeedRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long>, FeedRepositoryCustom {
    List<Feed> findAllByUserId(Long id);
}
