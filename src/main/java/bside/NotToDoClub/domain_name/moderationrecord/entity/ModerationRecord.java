package bside.NotToDoClub.domain_name.moderationrecord.entity;

import bside.NotToDoClub.domain_name.nottodo.entity.UserNotToDo;
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
@Table(name = "MODERATION_RECORD")
@EntityListeners(AuditingEntityListener.class)
public class ModerationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MODERATION_RECORD_ID")
    private Long id;

    @Lob
    @Column(name = "CONTENT")
    private String content;

    @Column(name = "SUCCESS_YN")
    private Boolean successYn;

    @Column(name = "RECORD_DATE")
    private LocalDateTime recordDate;

    @JoinColumn(name = "USER_NOT_TO_DO_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserNotToDo userNotToDo;

    @Column(name = "REG_DTM")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "MOD_DTM")
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
