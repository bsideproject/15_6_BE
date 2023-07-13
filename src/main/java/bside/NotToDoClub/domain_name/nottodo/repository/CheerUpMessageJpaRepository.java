package bside.NotToDoClub.domain_name.nottodo.repository;

import bside.NotToDoClub.domain_name.nottodo.entity.CheerUpMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheerUpMessageJpaRepository extends JpaRepository<CheerUpMessage, Long> {
}
