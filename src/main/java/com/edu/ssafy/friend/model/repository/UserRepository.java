package com.edu.ssafy.friend.model.repository;

import com.edu.ssafy.friend.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
