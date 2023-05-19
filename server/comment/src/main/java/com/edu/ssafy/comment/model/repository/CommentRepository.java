package com.edu.ssafy.comment.model.repository;

import com.edu.ssafy.comment.model.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comments, Long> {
}
