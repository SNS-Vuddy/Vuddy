package com.edu.ssafy.feed.model.repository;

import com.edu.ssafy.feed.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByNickname(String nickname);

    List<User> findAllByNicknameIn(List<String> nicknames);
}
