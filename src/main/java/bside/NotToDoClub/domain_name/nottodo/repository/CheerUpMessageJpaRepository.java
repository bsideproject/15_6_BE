package bside.NotToDoClub.domain_name.nottodo.repository;

import bside.NotToDoClub.domain_name.nottodo.entity.CheerUpMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CheerUpMessageJpaRepository extends JpaRepository<CheerUpMessage, Long> {
    Optional<CheerUpMessage> findByUserNotToDoIdAndDspOrder(Long userNotToDoId, int dspOrder);
}
