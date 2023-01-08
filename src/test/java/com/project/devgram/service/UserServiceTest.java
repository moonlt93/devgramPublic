package com.project.devgram.service;


import com.project.devgram.config.TestConfig;
import com.project.devgram.entity.Users;
import com.project.devgram.repository.UserRepository;
import com.project.devgram.type.ROLE;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@Import(TestConfig.class)
@DataJpaTest
class UserServiceTest {


    @Autowired
    private UserRepository userRepository;

    private Users user;

    @BeforeEach
    public void setUser() {

        Users user = Users.builder()
                .userSeq(1L)
                .username("testname")
                .role(ROLE.ROLE_USER)
                .build();
        userRepository.save(user);

    }

    @Test
    @DisplayName("회원 가입")
    void saveUser() {


        //when
        Optional<Users> optionalUsers = userRepository.findByUsername("testname");

        //then
        assertEquals("testname", optionalUsers.get().getUsername());
        System.out.println("saveUser test success");
    }

    @Test
    @DisplayName("회원가입한 user 체크")
    void getUserDetails() {
        //given

        //when
        Optional<Users> optionalUsers = userRepository.findById(2L);

        //then
        assertThrows(NoSuchElementException.class, () -> {
            System.out.println("get username : " + optionalUsers.get().getUsername());
        });


        System.out.println("getUserDetails test success");

    }

    @Test
    @DisplayName("유저 정보 업데이트")
    void updateUsersDetail() {
        //given
        Optional<Users> optionalUsers = userRepository.findById(1L);
        //when
        if (optionalUsers.isPresent()) {
            Users user = optionalUsers.get();

            user.setRole(ROLE.ROLE_ADMIN);
            user.setUsername("admin");

            userRepository.save(user);
        }
        Optional<Users> user = userRepository.findByUsername("admin");

        //then
        assertNotNull(user);
        assertEquals(ROLE.ROLE_ADMIN, user.get().getRole());


    }

    @Test
    @DisplayName("회원 철회")
    void deleteUserDetails() {
        //given

        //when
        Optional<Users> optionalUsers = userRepository.findByUsername("testname");
        optionalUsers.ifPresent(users -> userRepository.delete(users));
        //then
        Optional<Users> user = userRepository.findById(1L);

             assertThrows(NoSuchElementException.class, () -> {
             assertNull(user.get().getUserSeq());
        });


    }


}