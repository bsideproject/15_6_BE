package bside.NotToDoClub.domain_name.moderationrecord.repository;

import bside.NotToDoClub.domain_name.moderationrecord.dto.ModerationRecordResponseDto;
import bside.NotToDoClub.domain_name.moderationrecord.entity.ModerationRecord;
import bside.NotToDoClub.domain_name.nottodo.entity.UserNotToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ModerationRecordJpaRepository extends JpaRepository<ModerationRecord, Long> {

    /*@Query(value = "select m from ModerationRecord m join UserNotToDo u on m.userNotToDo.id = u.id where u.user.id = :userId and m.createdAt >= :fromDate and m.createdAt <= :toDate and m.useYn = true order by m.createdAt desc", nativeQuery = true)
    Optional<List<ModerationRecord>> findByFromDateAndEndDate(@Param("userId") Long userId, @Param("fromDate") String fromDate, @Param("toDate") String toDate);*/

    @Query(value = "select * from moderation_record m join user_not_to_do u on m.user_not_to_do_id = u.user_not_to_do_id where u.user_id = :userId and date_format(m.reg_dtm,'%Y%m%d') >= :fromDate and date_format(m.reg_dtm,'%Y%m%d') <= :toDate and m.use_yn = 'Y' order by m.reg_dtm desc", nativeQuery = true)
    Optional<List<ModerationRecord>> findByFromDateAndEndDate(@Param("userId") Long userId, @Param("fromDate") String fromDate, @Param("toDate") String toDate);
}
