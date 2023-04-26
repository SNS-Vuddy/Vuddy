package com.buddy.repository;

import com.buddy.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserRepository {

    private final EntityManager em;

    public void save(User user) {
        em.persist(user);
    }

    public User findByNickname(String nickname) {
        return em.createQuery("select u from User u where u.nickname = :nickname", User.class)
                .setParameter("nickname", nickname)
                .getSingleResult();
    }

    @Transactional
    public User updateStatusMessage(String userNickname, String statusMessage) {
        User user = findByNickname(userNickname);
//        user.setStatusMessage(statusMessage);

        System.out.println("======================================================");
        System.out.println("user.getStatusMessage() = " + user.getStatusMessage());
        System.out.println("user.getNickname() = " + user.getNickname());
        System.out.println("======================================================");
        em.merge(user);

        return user;
    }
}
