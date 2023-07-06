package bside.NotToDoClub.domain_name.nottodo.entity;

import bside.NotToDoClub.domain_name.user.entity.UserEntity;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USER_NOT_TO_DO")
@EntityListeners(AuditingEntityListener.class)
public class UserNotToDo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_NOT_TO_DO_ID")
    private Long id;

    @JoinColumn(name = "USER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @Column(name = "NOT_TO_DO_TEXT")
    private String notToDoText;

    @Column(name = "GOAL")
    private String goal;

    @Column(name = "PROGRESS_STATE")
    private String progressState;

    @Column(name = "SUCCESS_YN")
    private Boolean successYn;

    @Column(name = "START_DATE")
    private LocalDateTime startDate;

    @Column(name = "END_DATE")
    private LocalDateTime endDate;

    @Column(name = "TOTAL_CHALLENGE_DAYS")
    private Integer totalChallengeDays;

    @Column(name = "CURRENT_CHALLENGE_DAYS")
    private Integer currentChallengeDays;

    @Column(name = "USE_YN")
    private Boolean useYn;

    @Column(name = "REG_DTM")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "MOD_DTM")
    @LastModifiedDate
    private LocalDateTime updatedAt;

}
