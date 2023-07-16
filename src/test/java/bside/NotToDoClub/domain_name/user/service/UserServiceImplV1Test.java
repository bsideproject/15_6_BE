package bside.NotToDoClub.domain_name.user.service;

import bside.NotToDoClub.domain_name.user.dto.UserDto;
import bside.NotToDoClub.domain_name.user.entity.UserEntity;
import bside.NotToDoClub.domain_name.user.respository.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@Rollback(value = false)
@SpringBootTest
class UserServiceImplV1Test {

    @Autowired UserService userService;
    @Autowired UserLoginService userLoginService;
    @Autowired UserJpaRepository userJpaRepository;

    @Test @Rollback
    void updateUserNicknameTest(){
        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdW5zbGltMTFAZ21haWwuY29tIiwicm9sZXMiOiJVU0VSIiwiaWF0IjoxNjg4ODg3MDM3LCJleHAiOjE2OTE0NzkwMzd9.rwUvNDEy10ISKykL9KP6KDA62Vg2rgowj0VKjfkUzjI";


        UserDto userDto = userService.updateUserNickname(accessToken, "임준섭");

        UserDto loginUserInfo = userLoginService.getLoginUserInfo(accessToken);
        assertEquals(loginUserInfo.getNickname(), "임준섭");
    }
}