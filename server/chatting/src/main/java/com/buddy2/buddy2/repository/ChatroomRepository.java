package com.buddy2.buddy2.repository;

import com.buddy2.buddy2.entity.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
    Chatroom findByChatId(Long chatId);

//    @Query(value = "SELECT * FROM chatroom ORDER BY chat_id DESC LIMIT 1", nativeQuery = true)
    Chatroom findFirstByOrderByChatIdDesc();

    @Query(value = "SELECT c.* FROM user u INNER JOIN user_chatroom uc ON u.user_id = uc.user_id INNER JOIN chatroom c ON uc.chat_id = c.chat_id WHERE u.u_nickname = :nickname ORDER BY c.c_time DESC", nativeQuery = true)
    List<Chatroom> findWithNickname(@Param("nickname") String nickname);
}
