package bside.NotToDoClub.domain_name.inquiry.repository;

import bside.NotToDoClub.domain_name.inquiry.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryJpaRepository extends JpaRepository<Inquiry, Long> {
}
