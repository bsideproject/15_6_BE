package bside.NotToDoClub.domain_name.nottodo.repository;

import bside.NotToDoClub.domain_name.nottodo.entity.UserNotToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserNotToDoJpaRepository extends JpaRepository<UserNotToDo, Long> {
    Optional<List<UserNotToDo>> findAllByUserIdOrderByEndDate(Long userId);
    Optional<List<UserNotToDo>> findAllByUserIdOrderByEndDateDesc(Long userId);

    @Query("select n from UserNotToDo n where n.user.id = :userId and n.useYn = true order by n.endDate")
    Optional<List<UserNotToDo>> findByUserIdAndUseYnOrderByEndDate(Long userId);

    @Query("select n from UserNotToDo n where n.user.id = :userId and n.useYn = true order by n.endDate desc")
    Optional<List<UserNotToDo>> findByUserIdAndUseYnOrderByEndDateDesc(Long userId);

    @Query("select n from UserNotToDo n where n.id = :notToDoId and n.useYn = true")
    Optional<UserNotToDo> findByIdAndUseYn(Long notToDoId);

    int countUserNotToDoByUserId(Long userId);
}
