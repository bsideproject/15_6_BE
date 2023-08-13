package bside.NotToDoClub.domain_name.badge.repository;

import bside.NotToDoClub.domain_name.user.entity.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBadgeJpaRepository extends JpaRepository<UserBadge, Long> {
}
