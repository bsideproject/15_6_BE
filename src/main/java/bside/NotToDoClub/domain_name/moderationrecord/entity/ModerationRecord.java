package bside.NotToDoClub.domain_name.moderationrecord.entity;

import bside.NotToDoClub.domain_name.moderationrecord.dto.ModerationRecordCreateRequestDto;
import bside.NotToDoClub.domain_name.moderationrecord.dto.ModerationRecordCreateResponseDto;
import bside.NotToDoClub.domain_name.moderationrecord.dto.ModerationRecordUpdateRequestDto;
import bside.NotToDoClub.domain_name.nottodo.entity.CheerUpMessage;
import bside.NotToDoClub.domain_name.nottodo.entity.UserNotToDo;
import bside.NotToDoClub.global.BooleanToYNConverter;
import lombok.*;
import org.apache.catalina.User;
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

    @Column(name = "RECORD_TYPE")
    private String recordType;

    @Column(name = "RECORD_DATE")
    private LocalDateTime recordDate;

    @JoinColumn(name = "USER_NOT_TO_DO_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserNotToDo userNotToDo;

    @Column(name = "USE_YN")
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean useYn;

    /*@OneToMany(mappedBy = "moderationRecord")
    @Builder.Default
    private List<CheerUpMessage> cheerUpMessages = new ArrayList<>();*/

    @Column(name = "REG_DTM")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "MOD_DTM")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static ModerationRecord createModerationRecord(ModerationRecordCreateRequestDto moderationRecordCreateRequestDto, UserNotToDo userNotToDo){
        ModerationRecord moderationRecord = ModerationRecord.builder()
                .content(moderationRecordCreateRequestDto.getContent())
                .recordType(moderationRecordCreateRequestDto.getRecordType())
                .userNotToDo(userNotToDo)
                .build();

        return moderationRecord;
    }

    public void updateModerationRecord(ModerationRecordUpdateRequestDto moderationRecordUpdateRequestDto){
        this.content = moderationRecordUpdateRequestDto.getContent();
        this.recordType = moderationRecordUpdateRequestDto.getRecordType();
    }

    public void updateUseYn(){
        this.useYn = false;
    }
}
