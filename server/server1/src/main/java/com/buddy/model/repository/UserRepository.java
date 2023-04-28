package com.buddy.model.repository;

import com.buddy.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByNickname(String nickname);

    @Override
    Optional<User> findById(Long id);

}
