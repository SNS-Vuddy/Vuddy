package com.edu.ssafy.user.model.repository;

import com.edu.ssafy.user.model.entity.Feed;
import com.edu.ssafy.user.model.repository.custom.FeedRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, Long>, FeedRepositoryCustom {
}
