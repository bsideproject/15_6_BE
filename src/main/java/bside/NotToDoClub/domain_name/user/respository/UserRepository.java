package bside.NotToDoClub.domain_name.user.respository;

import bside.NotToDoClub.domain_name.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByLoginId(String loginId);
    boolean existsByNickname(String nickname);
    Optional<UserEntity> findByLoginId(String loginId);
}
