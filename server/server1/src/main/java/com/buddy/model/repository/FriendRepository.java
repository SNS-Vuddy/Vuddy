package com.buddy.model.repository;

import com.buddy.model.entity.Feed;
import com.buddy.model.entity.UserFriends;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<UserFriends, Long> {

}
