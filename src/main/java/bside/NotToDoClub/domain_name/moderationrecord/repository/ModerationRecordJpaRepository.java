package bside.NotToDoClub.domain_name.moderationrecord.repository;

import bside.NotToDoClub.domain_name.moderationrecord.dto.ModerationRecordListResponseDto;
import bside.NotToDoClub.domain_name.moderationrecord.entity.ModerationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ModerationRecordJpaRepository extends JpaRepository<ModerationRecord, Long> {

    @Query(value = "select m.moderation_record_id as moderationId, u.user_not_to_do_id as notToDoId, u.not_to_do_text as notToDoText, m.content as content, date_format(m.reg_dtm, '%Y-%m-%d %H:%m:%s') as regDtm, m.record_type as recordType, m.use_yn as useYn " +
            "from MODERATION_RECORD m " +
            "join USER_NOT_TO_DO u on m.user_not_to_do_id = u.user_not_to_do_id " +
            "where u.user_id = :userId " +
            "and date_format(m.reg_dtm,'%Y%m%d') >= :fromDate " +
            "and date_format(m.reg_dtm,'%Y%m%d') <= :toDate " +
            "and m.use_yn = 'Y' " +
            "and u.use_yn = 'Y' " +
            "order by m.reg_dtm desc", nativeQuery = true)
    Optional<List<ModerationRecordListResponseDto>> findByFromDateAndEndDate(@Param("userId") Long userId, @Param("fromDate") String fromDate, @Param("toDate") String toDate);

    @Query("select count(m) from ModerationRecord m where m.userNotToDo.user.id = :userId and m.useYn = true")
    int countModerationRecordByUserIdAndUseYn(Long userId);
}
