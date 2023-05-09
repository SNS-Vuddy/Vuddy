package com.edu.ssafy.comment.model.repository;

import com.edu.ssafy.comment.model.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, Long> {
}
