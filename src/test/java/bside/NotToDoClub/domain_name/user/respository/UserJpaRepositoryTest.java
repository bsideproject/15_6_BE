package bside.NotToDoClub.domain_name.user.respository;

import bside.NotToDoClub.domain_name.user.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserJpaRepositoryTest {

    @Autowired UserJpaRepository userJpaRepository;

    @Test
    void getUserTest(){

    }
}