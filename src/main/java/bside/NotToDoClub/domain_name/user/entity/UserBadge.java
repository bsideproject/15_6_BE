package bside.NotToDoClub.domain_name.user.entity;

import bside.NotToDoClub.domain_name.bedge.entity.Badge;
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
@Table(name = "USER_BADGE")
@EntityListeners(AuditingEntityListener.class)
public class UserBadge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_BADGE_ID")
    private Long id;

    @JoinColumn(name = "USER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @JoinColumn(name = "BADGE_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Badge badge;

    @Column(name = "REG_DTM")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "MOD_DTM")
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
