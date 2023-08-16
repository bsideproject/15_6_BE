package bside.NotToDoClub.domain_name.badge.repository;

import bside.NotToDoClub.domain_name.badge.entity.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BadgeJpaRepository extends JpaRepository<Badge, String> {

}
