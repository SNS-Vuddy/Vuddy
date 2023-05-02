package com.buddy.model.repository;

import com.buddy.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByNickname(String nickname);

    @Override
    Optional<User> findById(Long id);

    @Query("select u from User u where u.nickname in :nicknames")
    List<User> findAllByNicknames(@Param("nicknames") List<String> nicknames);

    List<User> findAllByNicknameIn(List<String> nicknames);
}
