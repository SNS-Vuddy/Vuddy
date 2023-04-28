package com.buddy.model.repository;

import com.buddy.model.entity.Feed;
import com.buddy.model.repository.custom.FeedRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long>, FeedRepositoryCustom {

    List<Feed> findAllByUserId(Long userId);

}
