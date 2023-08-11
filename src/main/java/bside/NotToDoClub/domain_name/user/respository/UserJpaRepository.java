package bside.NotToDoClub.domain_name.user.respository;

import bside.NotToDoClub.domain_name.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByLoginId(String loginId);
    boolean existsByNickname(String nickname);
    Optional<UserEntity> findByLoginId(String loginId);
    Optional<UserEntity> findByAccessToken(String token);
    @Transactional
    Optional<UserEntity> deleteByAccessToken(String token);

    @Transactional
    void deleteByLoginId(String loginId);
    @Query("select u from UserEntity u " +
            "join fetch u.userNotToDoList " +
            "where u.loginId = :loginId "
            )
    Optional<UserEntity> getUserNotToDoByLoginId(String loginId);
    @Query("select u from UserEntity u " +
            "join fetch u.cheerUpMessages "+
            "where u.loginId = :loginId ")
    Optional<UserEntity> getUserCheerUpListByLoginId(String loginId);

}
