package com.buddy.model.repository;

import com.buddy.model.dto.AllFriendDto;
import com.buddy.model.entity.User;
import com.buddy.model.entity.UserFriends;
import com.buddy.model.entity.enums.UserFriendStatus;
import com.buddy.model.repository.custom.FriendRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<UserFriends, Long>, FriendRepositoryCustom {

    boolean existsByRequestUserAndReceiveUser(User requester, User receiver);


    boolean existsByRequestUserAndReceiveUserAndStatusIs(User requester, User receiver, UserFriendStatus status);

    UserFriends findByRequestUserAndReceiveUser(User requester, User receiver);

    @Query("select new com.buddy.model.dto.AllFriendDto(u.nickname, u.profileImage) from UserFriends uf join uf.receiveUser u where uf.requestUser = :requester and uf.status = :status")
    List<AllFriendDto> findAllByRequestUserAndStatusIs(@Param("requester") User requester, @Param("status") UserFriendStatus status);

}
