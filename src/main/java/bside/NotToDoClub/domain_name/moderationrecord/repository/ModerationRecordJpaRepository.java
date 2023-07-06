package bside.NotToDoClub.domain_name.moderationrecord.repository;

import bside.NotToDoClub.domain_name.moderationrecord.entity.ModerationRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModerationRecordJpaRepository extends JpaRepository<ModerationRecord, Long> {
}
