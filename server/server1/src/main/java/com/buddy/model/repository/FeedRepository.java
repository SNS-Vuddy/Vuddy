package com.buddy.model.repository;

import com.buddy.model.dto.FeedWithTagsDto;
import com.buddy.model.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
    List<Feed> findAllByUserId(Long userId);

    @Query("SELECT new com.buddy.model.dto.FeedWithTagsDto(f, tf) FROM Feed f LEFT JOIN TaggedFriends tf ON f.id = tf.feed WHERE f.id = :id")
    List<FeedWithTagsDto> findOneWithTags(@Param("id") Long id);


}
