package bside.NotToDoClub.domain_name.inquiry.entity;

import bside.NotToDoClub.domain_name.user.entity.UserEntity;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "INQUIRY")
@EntityListeners(AuditingEntityListener.class)
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INQUIRY_ID")
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Setter
    @Column(name = "CONTENT")
    private String content;

    @Column(name = "REPLY_EMAIL")
    private String replyEmail;

    @JoinColumn(name = "USER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;


}
