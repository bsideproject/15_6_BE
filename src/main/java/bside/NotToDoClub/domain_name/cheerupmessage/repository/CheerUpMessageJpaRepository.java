package bside.NotToDoClub.domain_name.cheerupmessage.repository;

import bside.NotToDoClub.domain_name.cheerupmessage.entity.CheerUpMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheerUpMessageJpaRepository extends JpaRepository<CheerUpMessage, Long> {
}
