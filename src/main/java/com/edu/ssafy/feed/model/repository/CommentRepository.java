package com.edu.ssafy.feed.model.repository;

import com.edu.ssafy.feed.model.entity.Comments;
import com.edu.ssafy.feed.model.repository.custom.CommentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comments, Long>, CommentRepositoryCustom {
}
