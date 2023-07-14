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

    @Test
    void updateUserNicknameTest(){
        String accessToken = "8dsyP5MsZZS_LtEXQ1kjARo89vKhzq0DjVZ3d8AmCioljwAAAYkK4tdH";

        UserEntity sihun = new UserEntity().builder()
                .nickname("시훈")
                .loginId("osh0731@hanmail.net")
                .accessToken(accessToken)
                .build();

        UserEntity save = userJpaRepository.save(sihun);

        UserDto userDto = userService.updateUserNickname(accessToken, "지훈");

        UserDto loginUserInfo = userLoginService.getLoginUserInfo(accessToken);
        assertEquals(loginUserInfo.getNickname(), "지훈");
    }
}