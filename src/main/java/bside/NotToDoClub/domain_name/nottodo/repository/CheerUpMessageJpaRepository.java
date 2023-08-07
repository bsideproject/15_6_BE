package bside.NotToDoClub.domain_name.nottodo.repository;

import bside.NotToDoClub.domain_name.inquiry.entity.Inquiry;
import bside.NotToDoClub.domain_name.nottodo.entity.CheerUpMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CheerUpMessageJpaRepository extends JpaRepository<CheerUpMessage, Long> {
    Optional<CheerUpMessage> findByUserNotToDoIdAndDspOrder(Long userNotToDoId, int dspOrder);

    @Query("select c from CheerUpMessage c where c.registerUser.id = :userId")
    Optional<List<CheerUpMessage>> findByUserId(Long userId);
}
