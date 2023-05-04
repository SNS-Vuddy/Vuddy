package com.buddy3.buddy3.repository;

import com.buddy3.buddy3.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByNickname(String nickname);

    @Query(value = "SELECT u.u_nickname " +
            "FROM user u " +
            "INNER JOIN user_friends uf ON u.user_id = uf.requester_id OR u.user_id = uf.receiver_id " +
            "WHERE (uf.requester_id = :userId OR uf.receiver_id = :userId) AND uf.uf_status = \"ACCEPTED\" " +
            "AND u.user_id != :userId ", nativeQuery = true)
    List<String> findFriends(@Param("userId") Long userId);
}
