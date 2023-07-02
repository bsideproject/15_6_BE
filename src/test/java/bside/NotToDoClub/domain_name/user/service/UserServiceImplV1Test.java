package bside.NotToDoClub.domain_name.user.service;

import bside.NotToDoClub.domain_name.user.entity.UserEntity;
import bside.NotToDoClub.domain_name.user.respository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplV1Test {

    @Autowired UserService userService;
    @Autowired UserRepository userRepository;

    @Test @Rollback
    void getLoginUserByAccessTokenTest(){

        String accessToken = "abcde-abcde-abcde";

        UserEntity user = UserEntity.builder()
                .accessToken(accessToken)
                .nickname("sihun")
                .loginId("bside.sihun@gmail.com")
                .build();

        userRepository.save(user);


        UserEntity findUser = userRepository.findByAccessToken(accessToken).get();

        Assertions.assertThat(findUser.getNickname()).isEqualTo("sihun");
        Assertions.assertThat(findUser.getLoginId()).isEqualTo("bside.sihun@gmail.com");
    }
}