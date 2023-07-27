package bside.NotToDoClub.domain_name.nottodo.repository;

import bside.NotToDoClub.domain_name.nottodo.entity.ProgressState;
import bside.NotToDoClub.domain_name.nottodo.entity.UserNotToDo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserNotToDoJpaRepository extends JpaRepository<UserNotToDo, Long> {
    Optional<List<UserNotToDo>> findAllByUserIdOrderByEndDate(Long userId);
    Optional<List<UserNotToDo>> findAllByUserIdOrderByEndDateDesc(Long userId);
    int countUserNotToDoByUserId(Long userId);
}
