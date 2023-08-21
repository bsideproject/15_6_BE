package bside.NotToDoClub.domain_name.badge.repository;

import bside.NotToDoClub.domain_name.badge.dto.UserBadgeDto;
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

    @Query(value = "select b.badge_id as badgeId, b.name as badgeName, count(u.badge_id) as badgeCnt, b.image_url as imageUrl " +
            "from user_badge u " +
            "join badge b on u.badge_id = b.badge_id " +
            "where u.user_id = :userId " +
            "group by b.badge_id", nativeQuery = true)
    Optional<List<UserBadgeDto>> findAllUserBadge(long userId);
}
