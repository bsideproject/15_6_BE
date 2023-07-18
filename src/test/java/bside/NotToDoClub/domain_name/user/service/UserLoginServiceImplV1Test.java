package bside.NotToDoClub.domain_name.user.service;

import bside.NotToDoClub.domain_name.user.entity.UserEntity;
import bside.NotToDoClub.domain_name.user.respository.UserJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
class UserLoginServiceImplV1Test {

    @Autowired
    UserLoginService userService;
    @Autowired
    UserJpaRepository userRepository;

    @Test @Rollback
    void getLoginUserByAccessTokenTest(){

        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdW5zbGltMTFAZ21haWwuY29tIiwicm9sZXMiOiJVU0VSIiwiaWF0IjoxNjg4ODg3MDM3LCJleHAiOjE2OTE0NzkwMzd9.rwUvNDEy10ISKykL9KP6KDA62Vg2rgowj0VKjfkUzjI";

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