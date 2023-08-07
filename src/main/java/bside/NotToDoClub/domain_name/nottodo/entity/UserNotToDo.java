package bside.NotToDoClub.domain_name.nottodo.entity;

import bside.NotToDoClub.domain_name.moderationrecord.entity.ModerationRecord;
import bside.NotToDoClub.domain_name.nottodo.dto.NotToDoCreateRequestDto;
import bside.NotToDoClub.domain_name.nottodo.dto.NotToDoUpdateRequestDto;
import bside.NotToDoClub.domain_name.user.entity.UserEntity;
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
import java.util.Map;

@Entity
@Builder
@Getter
@ToString(exclude = {"user", "moderationRecords", "cheerUpMessages"})
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
    @Enumerated(EnumType.STRING)
    private ProgressState progressState;

    @Column(name = "SUCCESS_YN")
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean successYn;

    @Column(name = "START_DATE")
    private String startDate;

    @Column(name = "END_DATE")
    private String endDate;

    @Column(name = "TOTAL_CHALLENGE_DAYS")
    private Integer totalChallengeDays;

    @Column(name = "CURRENT_CHALLENGE_DAYS")
    private Integer currentChallengeDays;

    @Column(name = "USE_YN")
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean useYn;

    @OneToMany(mappedBy = "userNotToDo", fetch = FetchType.LAZY,
            orphanRemoval = true)
    @Builder.Default
    private List<ModerationRecord> moderationRecords = new ArrayList<>();

    @OneToMany(mappedBy = "userNotToDo", fetch = FetchType.LAZY,
            orphanRemoval = true)
    @Builder.Default
    private List<CheerUpMessage> cheerUpMessages = new ArrayList<>();

    @Column(name = "REG_DTM")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "MOD_DTM")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static UserNotToDo createUserNotToDo(NotToDoCreateRequestDto notToDoCreateRequestDto, UserEntity user, Map<Integer, String> cheerUpMsgMap){
        UserNotToDo userNotToDo = UserNotToDo.builder()
                .user(user)
                .notToDoText(notToDoCreateRequestDto.getNotToDoText())
                .goal(notToDoCreateRequestDto.getGoal())
                .progressState(ProgressState.BEFORE_START)
                .successYn(false)
                .startDate(notToDoCreateRequestDto.getStartDate())
                .endDate(notToDoCreateRequestDto.getEndDate())
                .useYn(true)
                .build();

        for (int i=1; i <= cheerUpMsgMap.size(); i++){
            CheerUpMessage newCheerUpMessage = CheerUpMessage.builder()
                    .dspOrder(i)
                    .content(cheerUpMsgMap.get(i))
                    .registerUser(user)
                    .useYn(true)
                    .build();

            //userNotToDo.addCheerUpMessage(newCheerUpMessage);
            newCheerUpMessage.setUserNotToDo(userNotToDo);
        }

        return userNotToDo;
    }

    /*public void addCheerUpMessage(CheerUpMessage cheerUpMessage){
        cheerUpMessage.setUserNotToDo(this);
        cheerUpMessages.add(cheerUpMessage);
    }

    public void setCheerUpMessage(CheerUpMessage cheerUpMessages){
        this.cheerUpMessages = cheerUpMessages;
        cheerUpMessages.setUserNotToDo(this);
    }*/

    public void updateCheerUpMessage(String cheerUpMessage, int idx){
        this.cheerUpMessages.get(idx).setContent(cheerUpMessage);
    }

    public void updateUserNotToDo(NotToDoUpdateRequestDto notToDoUpdateRequestDto,Map<Integer, String> cheerUpMsgMap){
        this.notToDoText = notToDoUpdateRequestDto.getNotToDoText();
        this.goal = notToDoUpdateRequestDto.getGoal();
        this.startDate = notToDoUpdateRequestDto.getStartDate();
        this.endDate = notToDoUpdateRequestDto.getEndDate();

        for (int i=0; i < cheerUpMsgMap.size(); i++){
            System.out.println("ch.get(i) = " + cheerUpMsgMap.get(i));
        }
    }

    public void updateUseYn(){
        this.useYn = false;
    }
}
