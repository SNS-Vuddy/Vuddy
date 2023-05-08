package com.edu.ssafy.friend.model.repository;

import com.edu.ssafy.friend.model.dto.AllFriendDto;
import com.edu.ssafy.friend.model.entity.User;
import com.edu.ssafy.friend.model.entity.UserFriends;
import com.edu.ssafy.friend.model.entity.enums.UserFriendStatus;
import com.edu.ssafy.friend.model.repository.custom.FriendRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<UserFriends, Long>, FriendRepositoryCustom {

    boolean existsByRequestUserAndReceiveUserAndStatusIs(User requester, User receiver, UserFriendStatus status);

    boolean existsByRequestUserAndReceiveUserAndStatusIsOrReceiveUserAndRequestUserAndStatusIs(User requester, User receiver, UserFriendStatus status1, User receiver2, User requester2, UserFriendStatus status2);

    UserFriends findByRequestUserAndReceiveUser(User requester, User receiver);


    @Query("select new com.edu.ssafy.friend.model.dto.AllFriendDto(u.nickname, u.profileImage) from UserFriends uf join uf.receiveUser u where uf.requestUser = :requester and uf.status = :status order by u.nickname asc")
    List<AllFriendDto> findAllByRequestUserAndStatusIs(@Param("requester") User requester, @Param("status") UserFriendStatus status);

}
