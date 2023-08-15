package bside.NotToDoClub.domain_name.badge.service;

import bside.NotToDoClub.domain_name.badge.repository.BadgeJpaRepository;
import bside.NotToDoClub.domain_name.moderationrecord.entity.ModerationRecord;
import bside.NotToDoClub.domain_name.moderationrecord.repository.ModerationRecordJpaRepository;
import bside.NotToDoClub.domain_name.nottodo.entity.ProgressState;
import bside.NotToDoClub.domain_name.nottodo.entity.UserNotToDo;
import bside.NotToDoClub.domain_name.nottodo.repository.UserNotToDoJpaRepository;
import bside.NotToDoClub.domain_name.user.entity.UserEntity;
import bside.NotToDoClub.domain_name.user.respository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BadgeBatchService {

    private final UserJpaRepository userJpaRepository;
    private final BadgeJpaRepository badgeJpaRepository;
    private final UserNotToDoJpaRepository userNotToDoJpaRepository;
    private final ModerationRecordJpaRepository moderationRecordJpaRepository;

    /**
     * 실패는 성공의 어머니 뱃지 부여 서비스
     */
    public void grantBadgeNo8(){
        // 성공의 어머니 뱃지 가져와야함
        // badgeJpaRepository

        List<UserNotToDo> userNotToDos = userNotToDoJpaRepository.findModerationRecordsByProgressState(ProgressState.IN_PROGRESS).orElseThrow(() -> {
            return new RuntimeException("앱 내 낫투두 서비스에 등록된 것이 없습니다.");
        });

        userNotToDos.forEach(userNotToDo -> {
            List<ModerationRecord> moderationRecords = userNotToDo.getModerationRecords();
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime today = now.minusHours(now.getHour())
                            .minusMinutes(now.getMinute())
                            .minusSeconds(now.getSecond());

            LocalDateTime yesterday = today.minusDays(1);

            List<ModerationRecord> todayModerationList = moderationRecords.stream().map(moderationRecord -> {
                LocalDateTime updatedAt = moderationRecord.getUpdatedAt();
                if (yesterday.isAfter(updatedAt) && updatedAt.isBefore(today)) {
                    return moderationRecord;
                }
                return null;
            }).collect(Collectors.toList());

            if(todayModerationList.size() == 0){
                UserEntity user = userNotToDo.getUser();
                // 가져온 뱃지 리스트로 userBadgeEntity 만들어야함
                // 그리고 저장하면 끝
            }
        });
    }
}
