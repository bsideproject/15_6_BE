package bside.NotToDoClub.domain_name.badge.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BADGE")
@EntityListeners(AuditingEntityListener.class)
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BADGE_ID")
    private Long id;

    /**
     * 뱃지 이름
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 서버 내 이미지 저장 경로
     * 절대 경로
     */
    @Column(name = "IMAGE_URL")
    private String imageUrl;

    /**
     * 뱃지 획득 조건
     */
    @Column(name = "QUALIFICATION")
    private String qualification;

    /**
     * 뱃지 설명
     */
    @Column(name = "EXPLANATION")
    private String explanation;

    /**
     * user - badge
     * N:M 관계 테이블 매핑을 위한 user-badge 테이블
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,
            orphanRemoval = true)
    @Builder.Default
    private List<UserBadge> users = new ArrayList<>();

    @Column(name = "REG_DTM")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "MOD_DTM")
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
