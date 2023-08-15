package bside.NotToDoClub.domain_name.nottodo.repository;

import bside.NotToDoClub.domain_name.nottodo.entity.UserNotToDo;
import bside.NotToDoClub.domain_name.user.entity.UserEntity;
import bside.NotToDoClub.domain_name.user.service.UserCommonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserNotToDoJpaRepositoryTest {

    @Autowired UserNotToDoJpaRepository userNotToDoJpaRepository;
    @Autowired
    UserCommonService userCommonService;


    @Test
    void findModerationRecordsByProgressState(){
        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoYXB5MTk5NUBuYXZlci5jb20iLCJyb2xlcyI6IlVTRVIiLCJpYXQiOjE2ODk2ODk3MDMsImV4cCI6MTY5MjI4MTcwM30.WgcCUcgPs1HoTaEtsJU5koNZc27SDw_fcUMxY8gSIts";

        UserEntity userEntity = userCommonService.checkUserByToken(accessToken);

    }
}