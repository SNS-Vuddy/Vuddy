package com.buddy.model.repository;

import com.buddy.model.entity.User;
import com.buddy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.runner.RunWith;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired EntityManager em;

    @Rollback(false)
    @Test
    void 회원가입() throws Exception {
        //given
        User user = User.createUserWithEmail("test1", "test1", "test1");
        System.out.println("====================================");
        System.out.println("user = " + user);
        System.out.println("====================================");

        //when
        em.persist(user);

        //then

    }
}