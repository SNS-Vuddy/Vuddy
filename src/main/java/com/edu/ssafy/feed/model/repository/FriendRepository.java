package com.edu.ssafy.feed.model.repository;

import com.edu.ssafy.feed.model.entity.UserFriends;
import com.edu.ssafy.feed.model.repository.custom.FriendRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepository extends JpaRepository<UserFriends, Long>, FriendRepositoryCustom {


}
