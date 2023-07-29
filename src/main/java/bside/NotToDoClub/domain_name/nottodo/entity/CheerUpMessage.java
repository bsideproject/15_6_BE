package bside.NotToDoClub.domain_name.nottodo.entity;

import bside.NotToDoClub.domain_name.moderationrecord.entity.ModerationRecord;
import bside.NotToDoClub.domain_name.user.entity.UserEntity;
import bside.NotToDoClub.global.BooleanToYNConverter;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CHEER_UP_MESSAGE")
@EntityListeners(AuditingEntityListener.class)
public class CheerUpMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHEER_UP_MESSAGE_ID")
    private Long id;

    /*@JoinColumn(name = "MODERATION_RECORD_ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ModerationRecord moderationRecord;*/

    @JoinColumn(name = "USER_NOT_TO_DO_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserNotToDo userNotToDo;

    @JoinColumn(name = "USER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity registerUser;

    @Lob
    @Column(name = "CONTENT")
    private String content;

    @Column(name = "DSP_ORDER")
    private int dspOrder;

    @Column(name = "USE_YN")
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean useYn;

    @Column(name = "REG_DTM")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "MOD_DTM")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void setUserNotToDo(UserNotToDo userNotToDo){
        this.userNotToDo = userNotToDo;
        userNotToDo.getCheerUpMessages().add(this);
    }

    public void updateContent(String content){
        this.content = content;
    }
}
