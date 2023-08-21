package bside.NotToDoClub.domain_name.badge.service;

import bside.NotToDoClub.domain_name.user.entity.UserEntity;
import bside.NotToDoClub.domain_name.user.respository.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BadgeBatchServiceTest {

    @Autowired BadgeBatchService badgeBatchService;
    @Autowired
    UserJpaRepository userJpaRepository;

    @Test
    void addMasterOfPatienceBadgeBatch(){

        UserEntity userEntity = userJpaRepository.findByLoginId("hapy1995@naver.com").get();

        badgeBatchService.addMasterOfPatienceBadgeBatch(userEntity);
    }
}