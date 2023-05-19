package com.edu.ssafy.friend.model.repository;

import com.edu.ssafy.friend.model.entity.User;
import com.edu.ssafy.friend.model.entity.UserFriends;
import com.edu.ssafy.friend.model.entity.enums.UserFriendStatus;
import com.edu.ssafy.friend.model.repository.custom.FriendRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepository extends JpaRepository<UserFriends, Long>, FriendRepositoryCustom {

    boolean existsByRequestUserAndReceiveUserAndStatusIs(User requester, User receiver, UserFriendStatus status);


    UserFriends findByRequestUserAndReceiveUser(User requester, User receiver);

}
