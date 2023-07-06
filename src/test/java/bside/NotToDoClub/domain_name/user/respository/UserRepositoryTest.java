package bside.NotToDoClub.domain_name.user.respository;

import bside.NotToDoClub.domain_name.user.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserJpaRepository userRepository;

    @Test
    @Rollback(value = true)
    void saveTest(){
        UserEntity userEntity = UserEntity.builder()
                .loginId("ohsh0731@daum.com")
                .nickname("sihun")
                .password("!@#$")
                .build();

        UserEntity saveUser = userRepository.save(userEntity);

        System.out.println(saveUser);

    }
}