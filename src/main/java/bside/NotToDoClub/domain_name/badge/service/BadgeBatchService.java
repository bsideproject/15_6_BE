package bside.NotToDoClub.domain_name.badge.service;

import bside.NotToDoClub.domain_name.badge.entity.UserBadge;
import bside.NotToDoClub.domain_name.badge.repository.BadgeJpaRepository;
import bside.NotToDoClub.domain_name.badge.repository.UserBadgeJpaRepository;
import bside.NotToDoClub.domain_name.moderationrecord.entity.ModerationRecord;
import bside.NotToDoClub.domain_name.nottodo.entity.ProgressState;
import bside.NotToDoClub.domain_name.nottodo.entity.UserNotToDo;
import bside.NotToDoClub.domain_name.nottodo.repository.UserNotToDoJpaRepository;
import bside.NotToDoClub.domain_name.user.entity.UserEntity;
import bside.NotToDoClub.domain_name.user.respository.UserJpaRepository;
import bside.NotToDoClub.domain_name.user.service.UserCommonService;
import bside.NotToDoClub.global.error.CustomException;
import bside.NotToDoClub.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
@Slf4j
public class BadgeBatchService {

    private final UserNotToDoJpaRepository userNotToDoJpaRepository;
    private final BadgeJpaRepository badgeJpaRepository;
    private final UserBadgeJpaRepository userBadgeJpaRepository;
    private final BadgeCommonService badgeCommonService;

    /**
     * ALL_MOD_REC_SECCESS 뱃지 추가
     */
    public void addMasterOfPatienceBadgeBatch(UserEntity userEntity){
       List<UserNotToDo> userNotToDos = getUserNotToDoList(userEntity);

        userNotToDos.forEach(userNotToDo -> {
            addMasterOfPatienceBadgeForUser(userEntity, userNotToDo);
        });
    }

    private void addMasterOfPatienceBadgeForUser(UserEntity userEntity, UserNotToDo userNotToDo) {
        Comparator<ModerationRecord> moderationRecordComparator = (mod1, mod2) ->
                mod1.getCreatedAt().compareTo(mod2.getCreatedAt());

        PriorityQueue<ModerationRecord> priorityQueue = new PriorityQueue<>(moderationRecordComparator);

        List<ModerationRecord> moderationRecords = userNotToDo.getModerationRecords();
        AtomicBoolean flag = new AtomicBoolean(true);
        AtomicBoolean successToday = isSuccessToday(priorityQueue, moderationRecords, flag);
        if(successToday.get()){
            /**
             * 겹치는거 없는지 유효성 체크
             */
            int overlapBit = userBadgeJpaRepository.countUserBadgeByBadgeId(
                    userEntity.getId(), BadgeList.MASTER_OF_PATIENCE.name());
            if(overlapBit == 0) {
                badgeCommonService.createUserBadge(userEntity, BadgeList.MASTER_OF_PATIENCE);
            }
        }
    }

    private List<UserNotToDo> getUserNotToDoList(UserEntity userEntity) {
        List<UserNotToDo> userNotToDos = userNotToDoJpaRepository
                .findMRByProgressStateAndNotToDoId(
                        ProgressState.IN_PROGRESS, userEntity.getId())
                .orElseThrow(() ->{
                    log.info("사용자 = {}", userEntity.getLoginId());
                    return new RuntimeException("진행중인 NotToDo가 없습니다.");
                });
        return userNotToDos;
    }

    private AtomicBoolean isSuccessToday(PriorityQueue<ModerationRecord> priorityQueue,
                                          List<ModerationRecord> moderationRecords,
                                          AtomicBoolean flag) {
        moderationRecords.forEach(moderationRecord -> {
            priorityQueue.add(moderationRecord);
            LocalDateTime yesterday = getYesterDayOclock();
            while (!priorityQueue.isEmpty()) {
                ModerationRecord current = priorityQueue.poll();

                if(current.getCreatedAt().isAfter(yesterday)){
                    if(current.getRecordType().equals("fail")){
                        flag.set(false);
                        break;
                    }
                } else{
                    break;
                }
            }
        });
        return flag;
    }

    private LocalDateTime getYesterDayOclock() {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();
        LocalDateTime yesterday = now.minusDays(1).minusHours(hour).minusMinutes(minute).minusSeconds(second);
        return yesterday;
    }
}
