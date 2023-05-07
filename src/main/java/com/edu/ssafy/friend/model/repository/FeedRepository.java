package com.edu.ssafy.friend.model.repository;

import com.edu.ssafy.friend.model.entity.Feed;
import com.edu.ssafy.friend.model.repository.custom.FeedRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, Long>, FeedRepositoryCustom {
}
