package com.edu.ssafy.user.model.repository;

import com.edu.ssafy.user.model.entity.User;
import com.edu.ssafy.user.model.repository.custom.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    User findByNickname(String nickname);

    List<User> findAllByNicknameIn(List<String> nicknames);

}
