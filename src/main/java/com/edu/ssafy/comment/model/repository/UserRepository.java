package com.edu.ssafy.comment.model.repository;

import com.edu.ssafy.comment.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
