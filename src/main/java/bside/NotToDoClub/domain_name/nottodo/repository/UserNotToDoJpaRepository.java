package bside.NotToDoClub.domain_name.nottodo.repository;

import bside.NotToDoClub.domain_name.nottodo.entity.ProgressState;
import bside.NotToDoClub.domain_name.nottodo.entity.UserNotToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserNotToDoJpaRepository extends JpaRepository<UserNotToDo, Long> {
    Optional<List<UserNotToDo>> findAllByUserIdOrderByEndDate(Long userId);
    Optional<List<UserNotToDo>> findAllByUserIdOrderByEndDateDesc(Long userId);

    @Query("select n from UserNotToDo n where n.user.id = :userId")
    Optional<List<UserNotToDo>> findByUserId(Long userId);

    @Query("select n from UserNotToDo n where n.user.id = :userId and n.useYn = true order by n.endDate")
    Optional<List<UserNotToDo>> findByUserIdAndUseYnOrderByEndDate(Long userId);

    @Query("select n from UserNotToDo n where n.user.id = :userId and n.useYn = true order by n.endDate desc")
    Optional<List<UserNotToDo>> findByUserIdAndUseYnOrderByEndDateDesc(Long userId);

    @Query("select n from UserNotToDo n where n.id = :notToDoId and n.useYn = true")
    Optional<UserNotToDo> findByIdAndUseYn(Long notToDoId);

    @Query("select count(n) from UserNotToDo n where n.user.id = :userId and n.useYn = true")
    int countUserNotToDoByUserIdAndUseYn(Long userId);

    @Query("select count(n) from UserNotToDo n where n.user.id = :userId and n.useYn = true and n.endDate >= current_date and n.startDate <= current_date ")
    int countUserNotToDoLimitCheck(Long userId);

    @Query("select n from UserNotToDo n " +
            "join fetch n.moderationRecords " +
            "where n.progressState = :progressState"
            )
    Optional<List<UserNotToDo>> findMRByProgressState(ProgressState progressState);

    @Query("select n from UserNotToDo n " +
            "join fetch n.moderationRecords " +
            "where n.progressState = :progressState " +
            "and n.id = :id")
    Optional<List<UserNotToDo>> findMRByProgressStateAndNotToDoId(ProgressState progressState, Long id);
}
