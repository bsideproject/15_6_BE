package bside.NotToDoClub.domain_name.nottodo.repository;

import bside.NotToDoClub.domain_name.nottodo.entity.UserNotToDo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserNotToDoRepository extends JpaRepository<UserNotToDo, Long> {
}
