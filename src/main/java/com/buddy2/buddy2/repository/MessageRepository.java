package com.buddy2.buddy2.repository;

import com.buddy2.buddy2.entity.Message;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query(value = "SELECT m FROM Message m WHERE m.chatId = :chatId AND m.time < :currentTime ORDER BY m.time DESC LIMIT 20", nativeQuery = true)
    List<Message> fiveMinuteBefore(@Param("chatId") Long chatId, @Param("currentTime") LocalDateTime currentTime);
}
