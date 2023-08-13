package bside.NotToDoClub.domain_name.badge.repository;

import bside.NotToDoClub.domain_name.badge.entity.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BadgeJpaRepository extends JpaRepository<Badge, Long> {

    @Query("select count(b) from UserBadge b where b.user.id = :userId and b.id = :badgeId")
    int countUserBadgeByBadgeId(long userId, long badgeId);
}
