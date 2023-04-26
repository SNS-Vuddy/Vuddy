package com.buddy.repository;

import com.buddy.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestRepository extends JpaRepository<User, Long> {

    User findByNickname(String nickname);
}
