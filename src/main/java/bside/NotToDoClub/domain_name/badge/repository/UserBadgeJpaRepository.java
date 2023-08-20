package bside.NotToDoClub.domain_name.badge.repository;

import bside.NotToDoClub.domain_name.badge.dto.UserBadgeResponseDto;
import bside.NotToDoClub.domain_name.user.entity.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserBadgeJpaRepository extends JpaRepository<UserBadge, Long> {

    @Query("select count(b) from UserBadge b where b.user.id = :userId and b.badge.id = :badgeName")
    int countUserBadgeByBadgeId(long userId, String badgeName);

    @Query("select b from UserBadge b join fetch b.badge where b.user.id = :userId")
    Optional<List<UserBadge>> findUserBadgeByUserId(long userId);

    @Query("select count(b.badge.id) as badgeCnt, b.badge.id as badgeName from UserBadge b join fetch b.badge where b.user.id = :userId group by b.badge.id")
    List<UserBadgeResponseDto> findAllUserBadge(long userId);
}
