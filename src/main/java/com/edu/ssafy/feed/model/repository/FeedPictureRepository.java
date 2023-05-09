package com.edu.ssafy.feed.model.repository;

import com.edu.ssafy.feed.model.entity.FeedPictures;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedPictureRepository extends JpaRepository<FeedPictures, Long> {
}
