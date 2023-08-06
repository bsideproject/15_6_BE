package bside.NotToDoClub.domain_name.inquiry.repository;

import bside.NotToDoClub.domain_name.inquiry.entity.Inquiry;
import bside.NotToDoClub.domain_name.nottodo.entity.UserNotToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InquiryJpaRepository extends JpaRepository<Inquiry, Long> {
    @Query("select i from Inquiry i where i.user.id = :userId")
    Optional<List<Inquiry>> findByUserId(Long userId);

}
