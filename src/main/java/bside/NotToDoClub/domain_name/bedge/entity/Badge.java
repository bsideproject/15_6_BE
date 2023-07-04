package bside.NotToDoClub.domain_name.bedge.entity;

import bside.NotToDoClub.domain_name.user.entity.UserBadge;
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

    @Column(name = "NAME")
    private String name;

    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @Column(name = "QUALIFICATION")
    private String qualification;

    @Column(name = "EXPLANATION")
    private String explanation;

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<UserBadge> users = new ArrayList<>();

    @Column(name = "REG_DTM")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "MOD_DTM")
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
