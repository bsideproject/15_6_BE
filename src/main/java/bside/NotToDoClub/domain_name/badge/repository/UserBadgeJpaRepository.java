package bside.NotToDoClub.domain_name.badge.repository;

import bside.NotToDoClub.domain_name.user.entity.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserBadgeJpaRepository extends JpaRepository<UserBadge, Long> {

    @Query("select count(b) from UserBadge b where b.user.id = :userId and b.badge.id = :badgeName")
    int countUserBadgeByBadgeId(long userId, String badgeName);
}
