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

    @Query(value =
            "select b.badge_id as badgeId, " +
                    "b.name as badgeName, " +
                    "count(u.badge_id) as badgeCnt, " +
                    "b.image_url as imageUrl, " +
                    "b.explanation as explanation," +
                    "b.qualification as qualification " +
                    "from BADGE b " +
                    "left outer join USER_BADGE u on u.badge_id = b.badge_id and u.user_id = :userId " +
                    "group by b.badge_id, u.user_id", nativeQuery = true)
    Optional<List<UserBadgeDto>> findAllUserBadge(long userId);

    @Query("select u from UserBadge u where u.user.id = :userId and u.badge.id = :badgeId")
    List<UserBadge> findUserBadgeByBadgeId(long userId, String badgeId);
}
