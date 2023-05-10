package com.buddy2.buddy2.repository;

import com.buddy2.buddy2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByNickname(String nickname);
}
