package bside.NotToDoClub.domain_name.badge.service;

import bside.NotToDoClub.domain_name.moderationrecord.entity.ModerationRecord;
import bside.NotToDoClub.domain_name.moderationrecord.repository.ModerationRecordJpaRepository;
import bside.NotToDoClub.domain_name.nottodo.entity.ProgressState;
import bside.NotToDoClub.domain_name.nottodo.entity.UserNotToDo;
import bside.NotToDoClub.domain_name.nottodo.repository.UserNotToDoJpaRepository;
import bside.NotToDoClub.domain_name.user.respository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BadgeBatchService {

    private final UserJpaRepository userJpaRepository;
    private final UserNotToDoJpaRepository userNotToDoJpaRepository;
    private final ModerationRecordJpaRepository moderationRecordJpaRepository;

    /**
     * 실패는 성공의 어머니 뱃지 부여 서비스
     */
    public void grantBadgeNo8(){
        List<UserNotToDo> userNotToDos = userNotToDoJpaRepository.findModerationRecordsByProgressState(ProgressState.IN_PROGRESS).orElseThrow(() -> {
            return new RuntimeException("앱 내 낫투두 서비스에 등록된 것이 없습니다.");
        });

        userNotToDos.forEach(userNotToDo -> {
            List<ModerationRecord> moderationRecords = userNotToDo.getModerationRecords();

        });
    }
}
